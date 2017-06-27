from flask import request, jsonify
from sqlalchemy import and_, extract

import json_model
from datetime import datetime, timedelta

from database import db_session
from models import journee, meteo, joueur, recette, produit, ingredient, zone
from client_controller import quit_game

def post_meteorology():
    json_model.meteoJsontoString = request.get_json(force=True)
    json_model.currentHour = json_model.meteoJsontoString['timestamp']
    if json_model.meteoJsontoString['timestamp']/24 != json_model.currentDay:
        #It's a new day
        m = json_model.get_or_create(db_session, meteo,  meteo_libelle=json_model.meteoJsontoString['weather'][0]['weather'])
        json_model.currentDay = json_model.meteoJsontoString['timestamp']/24
        newDay = datetime.now() + timedelta(days=json_model.currentDay)
        j = journee(newDay)
        m.journees.append(j)
        db_session.add(m)
        db_session.add(j)
        db_session.commit()
        kickPlayer()
        return play_actions()

    return jsonify(json_model.meteoJsontoString)

#curl -H "Content-Type: application/json" -X POST -d '{"timestamp": 1,"weather": [{"dfn": 0,"weather": "PLUIE"},{"dfn": 1,"weather": "CANICULE"}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/meteorology


def play_actions():
    json_model.actualRecettesNumberAndPrices.clear()
    for playerName, actions in json_model.tomorrowActions.iteritems():
        joueurDB = joueur.query.filter(joueur.joueur_pseudo == playerName).first()

        for action in actions['actions']:
            typeAction = action['kind']

            if typeAction == "recipe":
                # ajouter l'ajout de stand a la base de donnees
                nameRec = action['recipe']['name']
                composition = action['recipe']['ingredients']
                coutDev = len(composition)*len(composition)
                if coutDev <= joueurDB.joueur_budget :
                    joueurDB.joueur_budget -= coutDev
                    ingredients_nom = []
                    # recuperations des id de chaque ingredients
                    for x in composition:
                        ingredients_nom.append(x['name'])
                    ingredients = ingredient.query.filter(ingredient.ing_nom.in_(ingredients_nom)).all()
                    # ajout a la table possede des ingredients pour la recette
                    rec = recette(nameRec)
                    for x in ingredients:
                        rec.ingredients.append(x)
                    joueurDB.recettes.append(rec)
                    db_session.add(rec)
                    db_session.commit()

            elif typeAction == "ad":
                # ajouter l'ajout d'une pub a la base de donnees
                adX = action['location']['latitude']
                adY = action['location']['longitude']
                adRayon = float(action['rayon'])
                cost_ad = pow(adRayon, 1.8) / 2
                if joueurDB.joueur_budget > cost_ad:
                    joueurDB.joueur_budget -= cost_ad
                    advertisement = zone(adX, adY, adRayon, "ad")
                    joueurDB.zones.append(advertisement)
                    db_session.add(advertisement)
                    db_session.commit()

            elif typeAction == "drinks":
                # preparation

                # todo accept also when samples are sent by this way
                for key, act in action['prepare'].iteritems():
                    nomRecette = key
                    nbRecette = int(act)
                for key, act in action['price'].iteritems():
                    nomPrix = key
                    prix = act
                r = recette.query.filter(recette.recette_nom == nomRecette).first()
                hasAlcool = False
                isCold = False
                coutProd = 0
                for ing in r.ingredients:
                    coutProd += ing.ing_cout
                    if ing.ing_froid:
                        isCold = True
                    if ing.ing_alcohol:
                        hasAlcool = True

                actualDate = datetime.now() + timedelta(days=json_model.currentDay)
                jour = journee.query.filter(extract('day', journee.jour_date) == actualDate.day).first()

                total_cout_prod = (coutProd * nbRecette)

                if total_cout_prod > joueurDB.joueur_budget:
                    nbRecette = int(joueurDB.joueur_budget/coutProd)
                    total_cout_prod = nbRecette * coutProd
                joueurDB.joueur_budget -= total_cout_prod

                if joueurDB.joueur_pseudo not in json_model.actualRecettesNumberAndPrices:
                    json_model.actualRecettesNumberAndPrices[joueurDB.joueur_pseudo] = []

                json_model.actualRecettesNumberAndPrices[joueurDB.joueur_pseudo].append({"name": nomRecette, "price": prix, "hasAlcohol": hasAlcool, "isCold": isCold})
                # create parent, append a child via association
                prod = produit(nombre_prod=nbRecette, prix_vente=prix)
                prod.recette = r
                prod.journee = jour
                joueurDB.recettes_produit.append(prod)
                joueurDB.journees_produit.append(prod)
                db_session.add(prod)
                db_session.commit()

    json_model.nbVentesPlayer = {}
    json_model.tomorrowActions = {}
    return "Success"


def kickPlayer() :
    firstJoueur = joueur.query.first()
    if firstJoueur is not None : 
        joueurs = joueur.query.all()
        for j in joueurs :
            if j.joueur_pseudo in json_model.lastInfoFromPlayer :
                if json_model.currentHour - json_model.lastInfoFromPlayer[j.joueur_pseudo] >= 168 :
                    quit_game(j.joueur_pseudo)
            else :
                quit_game(j.joueur_pseudo)
