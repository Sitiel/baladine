from flask import request,jsonify

from database import db_session, Base, engine
from models import *


def map_get():
    joueurs = joueur.query.all()
    return jsonify(joueurs=[i.toJson() for i in joueurs])


def post_sales(sales):
    r = recette.query().filter(recette.recette_nom == sales['item'])
    total_cost = 0
    return r.toJson()
    for i in r:
        total_cost += i.ing_cout
    quantity = sales['quantity']
    #if sales['quantity'] > availablesItems['player'][sales['item']]:
    #    quantity = availablesItems['player'][sales['item']]

    t = transaction(quantity * total_cost)
    db_session.add(t)
    db_session.commit()
    return t.toJson()


def reset_game():
    Base.metadata.drop_all(bind=engine)
    Base.metadata.create_all(bind=engine)
    return "Success", 200, {'Content-Type': 'application/text'}

    # Example of recette creation with ingredients
    #u = ingredient('Citron', 0.1, False, True)


    #a = ingredient('Eau', 0, False, True)

    #db_session.add(u)
    #db_session.add(a)

    #db_session.commit()

    #x = recette("Limonade")
    #x.ingredients.append(u)
    #x.ingredients.append(a)

    #db_session.add(x)
    #db_session.commit()

    #return jsonify(ingredients_of_recette=[i.toJson() for i in x.ingredients])
