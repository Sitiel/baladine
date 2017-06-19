from flask import Flask, request, Response, jsonify
from flask_cors import CORS, cross_origin
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import *
import json
import random

# ----------------------INIT------------------------------ #

app = Flask(__name__)
app.debug = True
CORS(app)
app.config['SQLALCHEMY_DATABASE_URI'] = 'postgresql://localhost/partiel'
db = SQLAlchemy(app)

day = 0  # compteur de jour

budget = 1.0  # compteur d'argent
current_weather = "SUNNY"  # meteo du jour

requested_glasses = 0  # nombre de verres produits

COST_PER_GLASS = 0.15  # le cout de production
PRICE_PER_GLASS = 0.35  # le prix de vente

# les conditions meteo
WEATHER_VALUES = ["SUNNY AND HOT", "SUNNY", "CLOUDY", "RAINY"]

# la probabilite maximale (entre 0 et 1) de vente pour chaque condition meteo.
SALES_MAX = {
    "SUNNY AND HOT": 1.0,
    "SUNNY": 0.8,
    "CLOUDY": 0.5,
    "RAINY": 0.1
}

# la probabilite minimale (entre 0 et 1) de vente pour chaque condition meteo.
SALES_MIN = {
    "SUNNY AND HOT": 0.6,
    "SUNNY": 0.2,
    "CLOUDY": 0.0,
    "RAINY": 0.0
}


# ------------------DATABASES MODELS--------------------- #


class DAYS(db.Model):
    __tablename__ = "DAYS"
    id = db.Column(db.Integer, primary_key=True)
    day = db.Column(db.Integer)
    budget = db.Column(db.Integer)
    weather = db.Column(db.String(255))

    def __init__(self, day, budget, weather):
        self.day = day
        self.budget = budget
        self.weather = weather

    def __repr__(self):
        return '<Weather %r>' % self.weather

    @property
    def serialize(self):
        return {
            'id': self.id,
            'day': self.day,
            'budget': self.budget,
            'weather': self.weather,
        }


# -----------------------ROUTES--------------------------- #


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# Incremente le compteur de nombre de jours et selectionne aleatoirement une
# configuration meteo.
def moveToNextDay():
    global day
    day += 1

    global current_weather
    current_weather = random.choice(WEATHER_VALUES)


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# En fonction de la mete un nombre de ventes est choisi aleatoirement et le
# budget est mis a jour.
def simulateSales(requested_glasses):
    global budget

    proba = random.uniform(SALES_MIN[current_weather], SALES_MAX[current_weather])
    sales = int(requested_glasses * proba)

    expenses = requested_glasses * COST_PER_GLASS
    earnings = sales * PRICE_PER_GLASS

    budget += earnings - expenses

    return sales


@app.route('/getlastdays')
def getLastDays():
    days = DAYS.query.all()
    return jsonify(days=[i.serialize for i in days])


# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# Communique les informations globales de jeu
@app.route('/dayinfo')
def getDayInfo():
    return jsonify(day=day, budget=budget, weather=current_weather)


@app.route('/newgame')
def newGame():
    global day
    global budget
    global current_weather
    global requested_glasses
    day = 0  # compteur de jour

    budget = 1.0  # compteur d'argent
    current_weather = "SUNNY"  # meteo du jour

    requested_glasses = 0  # nombre de verres produits
    return '"Success."', 200, {'Content-Type': 'application/json'}


@app.route('/order', methods=['POST'])
def postOrder():
    # game over
    if budget < COST_PER_GLASS:
        # http status 412 = "Precondition Failed"
        return '"Insufficient funds."', 412, {'Content-Type': 'application/json'}

    # if not game over...
    sales = simulateSales(int(json.loads(request.data)['requested_glasses']))

    moveToNextDay()
    reg = DAYS(day, budget*100, current_weather)
    db.session.add(reg)
    db.session.commit()
    return jsonify(sales=sales)


# -------------------------START-------------------------- #

if __name__ == "__main__":
    app.run()
