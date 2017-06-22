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
    # Example of select *
    ingredients = ingredient.query.all()
    # Example of Model (row of table) to json conversion
    return jsonify(ingredients=[i.toJson() for i in ingredients])


def join_game(playerJoinUsername):
    j = joueur(playerJoinUsername['name'], 1.0)
    db_session.add(j)
    db_session.commit()
    return jsonify(playerJoinUsername)
#curl -H "Content-Type: application/json" -X POST -d '{"name": "Suskiki"}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/players

def map_player_name_get(playerName):
    return 'do some magic!'


def post_action(playerName, actions):
    typeAction = actions['actions'][0]['kind']
    if typeAction == "recipe" :
        #ajouter l'ajout de stand a la base de donnees
        #"recipe": {"name": "Limonade","ingredients": {},"hasAlcohol": false,"isCold": false}
        nameRec = actions['actions'][0]['recipe']['name']
        composition = actions['actions'][0]['recipe']['ingredients']
        ingredients_nom = []
        #recuperations des id de chaque ingredient
        for x in composition :
            ingredients_nom.append(x['name'])
        ingredients = ingredient.query.filter(ingredient.ing_nom.in_(ingredients_nom)).all()
        #ajout a la table possede des ingredients pour la recette
        #j = joueur(playerJoinUsername['name'], 1.0)
        joueurDB = joueur.query.filter(joueur.joueur_pseudo == playerName).one()
        #db_session.add(j)
        #db_session.commit()
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
