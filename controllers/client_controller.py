from database import db_session
from flask import jsonify
from models import *
from json_model import *


# - Fonction Flask - #

def get_meteorology():
    #jour = carte.journees[-1]
    #jour.meteo.meteo_libelle
    meteo = WeatherJson(0,'Sunny')
    metrology = MetrologyJson(0,meteo)
    
    return metrology.toJson()


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
    return 'do some magic!'


def map_player_name_get(playerName):
    return 'do some magic!'


def post_action(playerName, actions):
    return 'do some magic!'


def quit_game(playerName):
    return 'do some magic!'
