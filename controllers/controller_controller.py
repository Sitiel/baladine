from flask import request, jsonify
from sqlalchemy import and_, extract

import json_model
from datetime import datetime, timedelta

from database import db_session
from models import journee, meteo, joueur, recette, produit, ingredient, zone


def post_meteorology():
    json_model.meteoJsontoString = request.get_json(force=True)
    
    #meteoJson.metro_timestamp = meteo['timestamp']
    #meteoJson.metro_weather.append(WeatherJson(meteo['weather'][0]['dfn'],meteo['weather'][0]['weather']))

    if json_model.meteoJsontoString['timestamp']/24 != json_model.currentDay:
        m = json_model.get_or_create(db_session, meteo,  meteo_libelle=json_model.meteoJsontoString['weather'][0]['weather'])
        json_model.currentDay = json_model.meteoJsontoString['timestamp']/24
        newDay = datetime.now() + timedelta(days=json_model.currentDay)
        j = journee(newDay)
        m.journees.append(j)
        db_session.add(m)
        db_session.add(j)
        db_session.commit()
        play_actions()

    return jsonify(json_model.meteoJsontoString)

#curl -H "Content-Type: application/json" -X POST -d '{"timestamp": 1,"weather": [{"dfn": 0,"weather": "PLUIE"},{"dfn": 1,"weather": "CANICULE"}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/meteorology


def play_actions():

    for playerName, actions in json_model.tomorrowActions.iteritems():
        joueurDB = joueur.query.filter(joueur.joueur_pseudo == playerName).first()

        for action in actions['actions']:
            typeAction = action['kind']

            if typeAction == "recipe":
                # ajouter l'ajout de stand a la base de donnees
                nameRec = action['recipe']['name']
                composition = action['recipe']['ingredients']
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
                adRayon = action['rayon']
                advertisement = zone(adX, adY, adRayon, "ad")
                joueurDB.zones.append(advertisement)
                db_session.add(advertisement)
                db_session.commit()

            elif typeAction == "drinks":
                # preparation
                for key in action['prepare']:
                    nomRecette = key
                    nbRecette = action['prepare'][key]
                for key in action['price']:
                    nomPrix = key
                    prix = action['price'][key]

                recette_produit = recette.query.filter(
                    and_(recette.recette_nom == nomRecette, joueurDB.joueur_id == joueurDB.joueur_id)).first()
                actualDate = datetime.now() + timedelta(days=json_model.currentDay)
                jour = journee.query.filter(extract('day', journee.jour_date) == actualDate.day).first()

                # create parent, append a child via association
                prod = produit(nombre_prod=nbRecette, prix_vente=prix)
                prod.recette = recette_produit
                prod.journee = jour
                joueurDB.recettes_produit.append(prod)
                joueurDB.journees_produit.append(prod)
                db_session.add(prod)
                db_session.commit()
    json_model.tomorrowActions = {}