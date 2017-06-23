import random

from sqlalchemy.orm import load_only

from database import db_session
from flask import request,jsonify
from models import *
import json_model

# - Fonction Flask - #


def get_meteorology():
    #jour = carte.journees[-1]
    #jour.meteo.meteo_libelle
    #meteo = WeatherJson(0,'Sunny')
    #meteorology = MeteorologyJson(0,meteo)
    #return meteoJson.timestamp
    return jsonify(json_model.meteoJsontoString)



def ingredients_get():
    ingredients = ingredient.query.all()
    return jsonify(ingredients=[i.toJson() for i in ingredients])


def join_game(playerJoinUsername):
    j = joueur(playerJoinUsername['name'], 1.0)
    c = db_session.query(carte).first()
    rayon = 10
    latitude = (random.random()*(c.carte_largeur-rayon)+rayon)
    longitude = (random.random()*(c.carte_longueur-rayon)+rayon)
    location = {"latitude": latitude, "longitude": longitude}
    info = {"cash": 1, "sales": 0, "profit":0, "drinksOffered": [{"name": "Limonade", "price": 0.45, "hasAlcohol": False, "isCold": False}]}
    z = zone(latitude,longitude,rayon, "stand")
    c.joueurs.append(j)
    j.zones.append(z)
    db_session.add(j)
    db_session.add(z)
    db_session.commit()

    return jsonify({"name":playerJoinUsername['name'],"location": location,"info": info})
#curl -H "Content-Type: application/json" -X POST -d '{"name": "Suskiki"}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/players

def map_player_name_get(playerName):
    ingredients = ingredient.query.all()
    c = db_session.query(carte).first()
    region = {"center": {"latitude": 0, "longitude": 0}, "span": {"latitudeSpan": c.carte_largeur, "longitudeSpan": c.carte_longueur}}
    r = joueur.query.all()
    ranking = {"ranking": [i.getProp('joueur_pseudo') for i in r]}
    itemsByPlayer = {}
    for e_joueur in r:
        prop = []
        zones = e_joueur.zones
        for zone in zones:
            zone_location = {"latitude": zone.zone_posX, "longitude": zone.zone_posY}
            prop.append({"kind": zone.zone_type, "owner": e_joueur.getProp('joueur_pseudo'), "influence": zone.zone_rayon, "location": zone_location})
        itemsByPlayer[e_joueur.getProp('joueur_pseudo')] = prop
    final_map = {"region": region, "ranking": ranking, "itemsByPlayer": itemsByPlayer}
    info = {"cash": 1, "sales": 0, "profit": 0,
            "drinksOffered": [{"name": "Limonade", "price": 0.45, "hasAlcohol": False, "isCold": False}]}
    total = {'availablesIngredient': [i.toJson() for i in ingredients], 'map':final_map, "playerInfo": info}
    return total


def post_action(playerName, actions):
    typeAction = actions['actions'][0]['kind']
    if typeAction == "recipe" :
        #ajouter l'ajout de stand a la base de donnees
        nameRec = actions['actions'][0]['recipe']['name']
        composition = actions['actions'][0]['recipe']['ingredients']
        ingredients_nom = []
        #recuperations des id de chaque ingredients
        for x in composition :
            ingredients_nom.append(x['name'])
        ingredients = ingredient.query.filter(ingredient.ing_nom.in_(ingredients_nom)).all()
        #ajout a la table possede des ingredients pour la recette
        joueurDB = joueur.query.filter(joueur.joueur_pseudo == playerName).one()
        rec = recette(nameRec)
        for x in ingredients :
            rec.ingredients.append(x)    
        joueurDB.recettes.append(rec)
        db_session.add(rec)
        db_session.commit()

        return jsonify(ingredients=[i.toJson() for i in ingredients])
    elif typeAction == "ad" :
        #ajouter l'ajout d'une pub a la base de donnees
        
        return 'creation de pub'
    elif typeAction == "drinks" :
        #ajouter une recette
        return 'preparation de boissons'
    else :
        #error 400
        return "Error bad input", 400, {'Content-Type': 'application/text'}

    return jsonify(actions)
#curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "recipe","recipe": {"name": "Limonade","ingredients": [{"name": "Citron", "cost": 1,"hasAlcohol": false,"isCold": false}],"hasAlcohol": false,"isCold": false}}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/Suskiki

def quit_game(playerName):
    return 'do some magic!'
