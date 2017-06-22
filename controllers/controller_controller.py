from flask import request, jsonify
import json_model

def post_meteorology():
    json_model.meteoJsontoString = request.get_json(force=True)
    
    #meteoJson.metro_timestamp = meteo['timestamp']
    #meteoJson.metro_weather.append(WeatherJson(meteo['weather'][0]['dfn'],meteo['weather'][0]['weather']))
    return jsonify(json_model.meteoJsontoString)

#curl -H "Content-Type: application/json" -X POST -d '{"timestamp": 1,"weather": [{"dfn": 0,"weather": "PLUIE"},{"dfn": 1,"weather": "CANICULE"}]}' http://127.0.0.1:5000/ValerianKang/Balady_API/1.0.0/meteorology
