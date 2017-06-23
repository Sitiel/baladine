from datetime import datetime, timedelta
from flask import request,jsonify

from database import db_session, Base, engine
from models import *
import json_model

def map_get():
    joueurs = joueur.query.all()
    return jsonify(joueurs=[i.toJson() for i in joueurs])


def post_sales(sales):
    for s in sales['sales']:
        r = recette.query.filter(recette.recette_nom == s['item']).first()
        total_cost = 0
        for i in r.ingredients:
            total_cost += i.ing_cout
        quantity = s['quantity']
        t = transaction(quantity * total_cost)
        actualDate = datetime.now() + timedelta(days=json_model.currentDay)
        jour = journee.query.filter(extract('day', journee.jour_date) == actualDate.day).first()
        t.journee = jour
        j = joueur.query.filter(joueur.joueur_pseudo == s['player']).first()
        j.transactions.append(t)
        j.joueur_budget += (quantity*total_cost)
        db_session.add(t)

    db_session.commit()
    return "Success"



def reset_game():
    for tbl in reversed(Base.metadata.sorted_tables):
        engine.execute(tbl.delete())

    #Database seeding
    u = ingredient('Citron', 0.1, False, True)
    a = ingredient('Eau', 0, False, True)

    db_session.add(u)
    db_session.add(a)

    x = recette("Limonade")
    x.ingredients.append(u)
    x.ingredients.append(a)

    db_session.add(x)

    c = carte(1000,1000)
    today = datetime.now()
    j = journee(today)
    c.journees.append(j)
    db_session.add(c)
    db_session.add(j)

    db_session.commit()


    return "Success", 200, {'Content-Type': 'application/text'}

    # Example of recette creation with ingredients


    #return jsonify(ingredients_of_recette=[i.toJson() for i in x.ingredients])
