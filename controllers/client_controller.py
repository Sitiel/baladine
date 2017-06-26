import random

from sqlalchemy.orm import load_only

from datetime import datetime, timedelta
from database import db_session
from flask import request, jsonify
from  models import *
import json_model


# - Fonction Flask - #


def get_meteorology():
    # jour = carte.journees[-1]
    # jour.meteo.meteo_libelle
    # meteo = WeatherJson(0,'Sunny')
    # meteorology = MeteorologyJson(0,meteo)
    # return meteoJson.timestamp
    return jsonify(json_model.meteoJsontoString)


def ingredients_get():
    ingredients = ingredient.query.all()
    return jsonify(ingredients=[i.toJson() for i in ingredients])


def join_game(playerJoinUsername):
    """    """

    #Test pour savoir si UserName est deja utilise en ce moment
    name = playerJoinUsername['name']
    jExist = joueur.query.filter(joueur.joueur_pseudo == name).first()

    if jExist is None :
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

        return jsonify({"name": playerJoinUsername['name'], "location": location, "info": info})
    
    else :

        if json_model.currentHour - json_model.lastInfoFromPlayer[name] >= 36 :
        
            joueurStand = zone.query.filter(and_(zone.joueur_id == jExist.joueur_id, zone.zone_type == "stand")).first()
            location = {"latitude": joueurStand.zone_posX, "longitude": joueurStand.zone_posY}
        
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

        return "Joueur existe deja", 400, {'Content-Type': 'text/plain'}


# curl -H "Content-Type: application/json" -X POST -d '{"name": "Suskiki"}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/players

def map_player_name_get(playerName):
    json_model.lastInfoFromPlayer[playerName] = json_model.currentHour
    ingredients = ingredient.query.all()
    c = db_session.query(carte).first()
    region = {"center": {"latitude": 0, "longitude": 0},
              "span": {"latitudeSpan": c.carte_largeur, "longitudeSpan": c.carte_longueur}}
    r = joueur.query.all()
    ranking = {"ranking": [i.getProp('joueur_pseudo') for i in r]}
    itemsByPlayer = {}
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
    joueurDB = joueur.query.filter(joueur.joueur_pseudo == playerName).first()

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


def post_action(playerName, actions):
    json_model.tomorrowActions[playerName] = actions
    return "Success", 200, {'Content-Type': 'text/plain'}


def chat_get():
    return jsonify(json_model.lastMessages)


def chat_post(chatMessage):
    json_model.lastMessages.append(chatMessage)
    return "Success", 200, {'Content-Type': 'text/plain'}


# recette
# curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "recipe","recipe": {"name": "Limonade","ingredients": [{"name": "Citron", "cost": 1,"hasAlcohol": false,"isCold": false}],"hasAlcohol": false,"isCold": false}}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Coco
# pub
# curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "ad", "location": {"latitude": 50,"longitude": 60},"rayon": 50}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Coco
# preparation
# curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "drinks", "prepare": {"Limonade": 15},"price": {"Limonade" : 50.0}}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Coco

# 3 d'un coup !
# curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "recipe","recipe": {"name": "Limonade","ingredients": [{"name": "Citron", "cost": 1,"hasAlcohol": false,"isCold": false}],"hasAlcohol": false,"isCold": false}}, {"kind": "ad", "location": {"latitude": 50,"longitude": 60},"rayon": 50}, {"kind": "drinks", "prepare": {"Limonade": 15},"price": {"Limonade" : 50.0}}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Coco

def quit_game(playerName):
    joueurDB = joueur.query.filter(joueur.joueur_pseudo == playerName).first()
    productions = produit.query.filter(produit.joueur_id == joueurDB.joueur_id).all()
    participation = participe.query.filter(participe.joueur_id == joueurDB.joueur_id).all()
    zones = zone.query.filter(zone.joueur_id == joueurDB.joueur_id).all()


    # partie joueur
    for prod in productions :
        db_session.delete(prod)
    for part in participation :
        db_session.delete(part)
    for zon in zones :
        db_session.delete(zon)

    #partie recette liee au joueur
    possedes = possede.query.filter(possede.joueur_id == joueurDB.joueur_id).all()
    for pos in possedes :
        recet = recette.query.filter(pos.recette_id == recette.recette_id).first()
        db_session.delete(pos)
        composition = compose.query.filter(compose.recette_id == recet.recette_id).all()
        for comp in composition :
            db_session.delete(comp)
        db_session.delete(recet)
    db_session.delete(joueurDB)
    db_session.commit()
    return 'Success'
