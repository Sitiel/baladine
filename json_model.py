

# - Class JSON - #
class JsonModel():
    def toJson(self):
        dict = self.__dict__
        dictio = {}
        invalid_keys = {"_sa_instance_state"}
        for x in dict :
            if x not in invalid_keys :
                if isinstance(dict[x],JsonModel):
                    dictio[x.rsplit('_', 1)[-1]] = dict[x].toJson()
                else :
                    dictio[x.rsplit('_', 1)[-1]] = dict[x]
        return dictio

    def getProp(self, propertie):
        return self.__dict__[propertie]


class MeteorologyJson(JsonModel):
    metro_timestamp = 0
    metro_weather = []

    def __init__(self, timestamp, weather):
        self.metro_timestamp = timestamp
        self.metro_weather = weather


class WeatherJson(JsonModel):
    weather_dfn = 0
    weather_type = 0

    def __init__(self, dfn, wtype):
        self.weather_dfn = dfn
        self.weather_type = wtype


def get_or_create(session, model, **kwargs):
    instance = session.query(model).filter_by(**kwargs).first()
    if instance:
        return instance
    else:
        instance = model(**kwargs)
        session.add(instance)
        session.commit()
        return instance

meteoJson = MeteorologyJson(0, 0)
meteoJsontoString = 0

currentDay = 0


availablesItems = []
tomorrowActions = {}
nbVentesPlayer = {}
lastMessages = []