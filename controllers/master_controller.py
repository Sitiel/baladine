# coding=utf-8
# coding=utf-8
# coding=utf-8
# coding=utf-8
from datetime import datetime, timedelta
from flask import request,jsonify

from database import db_session, Base, engine
from models import *
import json_model

def map_get():
    ingredients = ingredient.query.all()
    c = db_session.query(carte).first()
    region = {"center": {"latitude": 0, "longitude": 0}, "span": {"latitudeSpan": c.carte_largeur, "longitudeSpan": c.carte_longueur}}
    r = joueur.query.all()
    ranking = {"ranking": [i.getProp('joueur_pseudo') for i in r]}
    itemsByPlayer = {}
    additionalPropPlayerInfo = {}
    drinksByPlayer = {}

    for e_joueur in r:
        prop = []
        zones = e_joueur.zones
        for zone in zones:
            zone_location = {"latitude": zone.zone_posX, "longitude": zone.zone_posY}
            prop.append({"kind": zone.zone_type, "owner": e_joueur.getProp('joueur_pseudo'), "influence": zone.zone_rayon, "location": zone_location})

        itemsByPlayer[e_joueur.getProp('joueur_pseudo')] = prop

        propPlayerInfoRecettes = []
        propDrinksByPlayer = []
        for recette in e_joueur.recettes:
            isCold = False
            hasAlcohol = False
            price = 0
            for i in recette.ingredients:
                if i.ing_froid:
                    isCold = True
                if i.ing_alcohol:
                    hasAlcohol = True
                price += i.ing_cout
            propPlayerInfoRecettes.append({"name": recette.recette_nom, "price": price, "hasAlcohol": hasAlcohol, "isCold": isCold})
            propDrinksByPlayer.append({"name": recette.recette_nom, "price": price, "hasAlcohol": hasAlcohol, "isCold": isCold})

        propPlayerProperties = {"cash": e_joueur.joueur_budget, "sales": 0, "profit": 0, "drinksOffered": propPlayerInfoRecettes}
        additionalPropPlayerInfo[e_joueur.getProp('joueur_pseudo')] = propPlayerProperties
        drinksByPlayer[e_joueur.getProp('joueur_pseudo')] = propDrinksByPlayer

    final_map = {"region": region, "ranking": ranking, "itemsByPlayer": itemsByPlayer, "playerInfo": additionalPropPlayerInfo, "drinksByPlayer": drinksByPlayer}
    return final_map


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
    db_session.add(ingredient('Café', 0.5, False, False))
    db_session.add(ingredient('Chocolat', 0.22, False, False))
    db_session.add(ingredient('Rhum', 6, True, True))
    db_session.add(ingredient('Sucre', 0.1, False, False))
    db_session.add(ingredient('Alcool', 10, True, True))
    db_session.add(ingredient('Jus d\'orange', 0.5, False, True))
    db_session.add(ingredient('Gin', 0.3, True, True))
    db_session.add(ingredient('Vodka', 5, True, True))
    db_session.add(ingredient('Jus de grenadine', 0.2, False, True))
    db_session.add(ingredient('Sirop de menthe', 0.2, False, True))
    db_session.add(ingredient('Jus de banane', 0.33, False, True))
    db_session.add(ingredient('Glaçons', 0.02, False, True))
    db_session.add(ingredient('Sel', 0.1, False, True))
    db_session.add(ingredient('Colorant', 0.2, False, True))
    db_session.add(ingredient('Infusion de pêche', 0.6, False, True))
    db_session.add(ingredient('Feuilles de thé', 2, False, True))
    db_session.add(ingredient('Feuilles de Kola', 5, False, True))
    db_session.add(ingredient('Lait', 1, False, True))


    db_session.add(u)
    db_session.add(a)

    x = recette("Limonade")
    x.ingredients.append(u)
    x.ingredients.append(a)

    db_session.add(x)

    c = carte(1000, 1000)
    today = datetime.now()
    j = journee(today)
    c.journees.append(j)
    db_session.add(c)
    db_session.add(j)

    db_session.commit()


    return "Success", 200, {'Content-Type': 'application/text'}

    # Example of recette creation with ingredients


    #return jsonify(ingredients_of_recette=[i.toJson() for i in x.ingredients])
