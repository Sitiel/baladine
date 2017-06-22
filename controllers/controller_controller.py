from flask import request, jsonify


def post_meterology():
    meteorology = request.get_json(force=True)
