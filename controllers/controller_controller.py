from flask import request, jsonify
import json_model
from datetime import datetime, timedelta

from database import db_session
from models import journee, meteo


def post_meteorology():
    json_model.meteoJsontoString = request.get_json(force=True)
    
    #meteoJson.metro_timestamp = meteo['timestamp']
    #meteoJson.metro_weather.append(WeatherJson(meteo['weather'][0]['dfn'],meteo['weather'][0]['weather']))

    if json_model.meteoJsontoString['timestamp']/24 != json_model.currentDay:
        m = json_model.get_or_create(db_session, meteo,  meteo_libelle=json_model.meteoJsontoString['weather'][0]['weather'])
        json_model.currentDay = json_model.meteoJsontoString['timestamp']/24
        newDay = datetime.now() + timedelta(days=json_model.currentDay)
        j = journee(newDay)
        m.journees.append(j)
        db_session.add(m)
        db_session.add(j)
        db_session.commit()

    return jsonify(json_model.meteoJsontoString)

#curl -H "Content-Type: application/json" -X POST -d '{"timestamp": 1,"weather": [{"dfn": 0,"weather": "PLUIE"},{"dfn": 1,"weather": "CANICULE"}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/meteorology
