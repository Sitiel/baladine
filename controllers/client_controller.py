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
    # Example of insert into
    u = ingredient('Citron', 0.1, False, True)
    db_session.add(u)
    db_session.commit()
    # Example of select *
    ingredients = ingredient.query.all()
    # Example of Model (row of table) to json conversion
    return jsonify(ingredients=[i.toJson() for i in ingredients])


def join_game(playerJoinUsername):
    j = joueur(playerJoinUsername['name'], 1.0)
    db_session.add(j)
    db_session.commit()
    return jsonify(playerJoinUsername)


def map_player_name_get(playerName):
    return 'do some magic!'


def post_action(playerName, actions):
    typeAction = actions['actions'][0]['kind']
    if typeAction == "recipe" :
        #ajouter l'ajout de stand a la base de donnees
        #"recipe": {"name": "Limonade","ingredients": {},"hasAlcohol": false,"isCold": false}
        nameRec = actions['actions'][0]['name']
        composition = actions['actions'][0]['ingredients']
        ingredients = ingredient.query.with_entities(ingredient.ing_id, ingredient.ing_nom)
        return jsonify(ingredients)
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
#curl -H "Content-Type: application/json" -X POST -d '{"actions": [{"kind": "stand","recipe": {"name": "Limonade","ingredients": {},"hasAlcohol": false,"isCold": false},"location": {"latitude": 0,"longitude": 0},"prepare": {}}],"simulated": false}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/actions/suskiki

def quit_game(playerName):
    return 'do some magic!'
