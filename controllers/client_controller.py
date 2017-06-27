import random

from sqlalchemy.orm import load_only

from datetime import datetime, timedelta
from database import db_session
from flask import request, jsonify, json
from models import *
import json_model


# - Fonction Flask - #

"""
Cette route est utilise par tous les clients(web et java)
pour recuperer l'heure et la meteo
"""
def get_meteorology():
    #meteoJsontoString contient les infos recut par la carte arduino en json
    return jsonify(json_model.meteoJsontoString)

"""
Cette route est utilise par tous les clients(web et java)
pour recuperer tous les informations sur les ingredients du jeu
"""
def ingredients_get():
    ingredients = ingredient.query.all()
    return jsonify(ingredients=[i.toJson() for i in ingredients])

"""
join_game est utilise par tous les clients web pour rejoindre une partie en cours
elle peux aussi servir a se reconnecter lorsqu'on a ete deconnecter (delai de 36h dans le jeu)
"""
def join_game(playerJoinUsername):

    #Test pour savoir si UserName est deja utilise en ce moment
    name = playerJoinUsername['name']
    #jExist variable pour savoir si le joueur existe deja
    jExist = joueur.query.filter(joueur.joueur_pseudo == name).first()
    if jExist is None :
        #Si le joueur n'existe pas on le creer et on l'ajoute a la BD
        j = joueur(playerJoinUsername['name'], 1.0)
        c = db_session.query(carte).first()
        rayon = 10
        latitude = (random.random() * (c.carte_largeur - rayon) + rayon)
        longitude = (random.random() * (c.carte_longueur - rayon) + rayon)
        location = {"latitude": latitude, "longitude": longitude}
        info = {"cash": 1, "sales": 0, "profit": 0,
                "drinksOffered": [{"name": "Limonade", "price": 0.45, "hasAlcohol": False, "isCold": False}]}
        z = zone(latitude, longitude, rayon, "stand")
        rec = recette.query.filter(recette.recette_nom == 'Limonade').one()
        j.recettes.append(rec)

        c.joueurs.append(j)
        j.zones.append(z)
        db_session.add(j)
        db_session.add(z)
        db_session.commit()
        json_model.lastInfoFromPlayer[name] = json_model.currentHour

        return jsonify({"name": playerJoinUsername['name'], "location": location, "info": info})
    
    else :
        #Sinon on regarde combien de temps c'est passe depuis la dernier action du joueur 
        if json_model.currentHour - json_model.lastInfoFromPlayer[name] >= 36:
            #Si 36h sont passees on reconnect sur ce joueur en recuperant ces informations
            joueurStand = zone.query.filter(and_(zone.joueur_id == jExist.joueur_id, zone.zone_type == "stand")).first()
            location = {"latitude": joueurStand.zone_posX, "longitude": joueurStand.zone_posY}
            drinksOffered = []
            for r in jExist.recettes:
                isCold = False
                hasAlcohol = False
                price = 0
                for i in r.ingredients:
                    if i.ing_froid:
                        isCold = True
                    if i.ing_alcohol:
                        hasAlcohol = True
                    price += i.ing_cout
                drinksOffered.append({"name": r.recette_nom, "price": price, "hasAlcohol": hasAlcohol, "isCold": isCold})
            
            info = {"cash": jExist.joueur_budget, "sales": 0, "profit": 0,
                    "drinksOffered": drinksOffered}
            
            return jsonify({"name": playerJoinUsername['name'], "location": location, "info": info})

        #Sinon on ce connecte pas la personne
        return "Joueur existe deja", 400, {'Content-Type': 'text/plain'}


# curl -H "Content-Type: application/json" -X POST -d '{"name": "Suskiki"}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/players

"""
Cette route est utilise pour recuperer les informations du joueur pour la partie actuel
"""
def map_player_name_get(playerName):
    
    joueurDB = joueur.query.filter(joueur.joueur_pseudo == playerName).first()
    json_model.lastInfoFromPlayer[playerName] = json_model.currentHour
    ingredients = ingredient.query.all()
    c = db_session.query(carte).first()

    region = {"center": {"latitude": 0, "longitude": 0},
              "span": {"latitudeSpan": c.carte_largeur, "longitudeSpan": c.carte_longueur}}
    
    r = joueur.query.all()
    rankedPlayer = joueur.query.order_by(joueur.joueur_budget.desc()).all()
    ranking = {"ranking": [i.getProp('joueur_pseudo') for i in rankedPlayer]}
    itemsByPlayer = {}
    
    #Recuperation des donnees stand et pub possede par le joueur
    for e_joueur in r:
        prop = []
        zones = e_joueur.zones
    
        for zone in zones:
            zone_location = {"latitude": zone.zone_posX, "longitude": zone.zone_posY}
            prop.append(
                {"kind": zone.zone_type, "owner": e_joueur.getProp('joueur_pseudo'), "influence": zone.zone_rayon,
                 "location": zone_location})

        itemsByPlayer[e_joueur.getProp('joueur_pseudo')] = prop

    final_map = {"region": region, "ranking": ranking, "itemsByPlayer": itemsByPlayer}
    drinksOffered = []
    
    #Recuperation des donnees sur les recettes possedes par le joueur
    for r in joueurDB.recettes:
        isCold = False
        hasAlcohol = False
        price = 0
    
        for i in r.ingredients:
            if i.ing_froid:
                isCold = True
            if i.ing_alcohol:
                hasAlcohol = True
            price += i.ing_cout
    
        drinksOffered.append({"name": r.recette_nom, "price": price, "hasAlcohol": hasAlcohol, "isCold": isCold})
    
    info = {"cash": joueurDB.joueur_budget, "sales": 0, "profit": 0,
            "drinksOffered": drinksOffered}
    
    total = {'availablesIngredient': [i.toJson() for i in ingredients], 'map': final_map, "playerInfo": info}
    
    return total


"""
Cette route est utilise pour poster les actions qui seront faite le lendemain
"""
def post_action(playerName, actions):
    
    simulated = False

    if 'simulated' not in actions :
        
        simulated = False
        json_model.tomorrowActions[playerName] = actions
        return "Success", 200, {'Content-Type': 'text/plain'}
    
    else :
        
        simulated = actions['simulated']
    
        if simulated == False :
    
            json_model.tomorrowActions[playerName] = actions
            return "Success", 200, {'Content-Type': 'text/plain'}
            
        totalCost = 0    
        for playerName, actions in json_model.tomorrowActions.iteritems():
    
            for action in actions['actions']:
                composition = actions['recipe']['ingredients']
                coutDev = len(composition.ingredients)*len(composition.ingredients)

        return {"sufficientFunds": False, "totalCost": 9999999}


"""
Cette route recupere les messages envoyes
"""
def chat_get():
    return jsonify(json_model.lastMessages)


"""
Cette route permet d'envoyer des messages
"""
def chat_post(chatMessage):
    json_model.lastMessages.append(chatMessage)
    return "Success", 200, {'Content-Type': 'text/plain'}


# recette
# curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "recipe","recipe": {"name": "Limonade","ingredients": [{"name": "Citron", "cost": 1,"hasAlcohol": false,"isCold": false}],"hasAlcohol": false,"isCold": false}}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Coco
# pub
# curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "ad", "location": {"latitude": 50,"longitude": 60},"rayon": 50}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Coco
# preparation
# curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "drinks", "prepare": {"Limonade": 15},"price": {"Limonade" : 50.0}}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Coco

# 3 d'un coup
# curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "recipe","recipe": {"name": "Limonade","ingredients": [{"name": "Citron", "cost": 1,"hasAlcohol": false,"isCold": false}],"hasAlcohol": false,"isCold": false}}, {"kind": "ad", "location": {"latitude": 50,"longitude": 60},"rayon": 50}, {"kind": "drinks", "prepare": {"Limonade": 15},"price": {"Limonade" : 50.0}}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Coco


"""
Cette route permet de supprimer le joueur donnees
"""
def quit_game(playerName):
    joueurDB = joueur.query.filter(joueur.joueur_pseudo == playerName).first()
    productions = produit.query.filter(produit.joueur_id == joueurDB.joueur_id).all()
    participation = joueurDB.transactions
    zones = zone.query.filter(zone.joueur_id == joueurDB.joueur_id).all()


    # partie joueur
    for prod in productions :
        db_session.delete(prod)
        db_session.commit()
    participation[:] = []
    db_session.commit()
    for zon in zones :
        db_session.delete(zon)
        db_session.commit()

    #partie recette liee au joueur
    for pos in joueurDB.recettes :
        if pos.recette_nom != 'Limonade' :
            composition = pos.ingredients
            composition[:] = []
            db_session.commit()

    joueurDB.recettes[:] = []
    db_session.commit()
    db_session.delete(joueurDB)
    db_session.commit()

    return 'Success'

#curl -X DELETE "http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/players/Coco"