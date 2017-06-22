

# - Class JSON - #
class JsonModel():
    def toJson(self):
        dict = self.__dict__
        dictio = {}
        invalid_keys = {"_sa_instance_state"}
        for x in dict :
            if x not in invalid_keys :
                if isinstance(dict[x],JsonModel):
                    print("Json")
                    dictio[x.rsplit('_', 1)[-1]] = dict[x].toJson()
                else :
                    print("Not Json")
                    dictio[x.rsplit('_', 1)[-1]] = dict[x]
        return dictio
        #return {x.rsplit('_', 1)[-1]: dict[x] for x in dict if x not in invalid_keys}


class MetrologyJson(JsonModel):
    metro_timestamp = 0
    metro_weather = 0

    def __init__(self, timestamp, weather):
        self.metro_timestamp = timestamp
        self.metro_weather = weather


class WeatherJson(JsonModel):
    weather_dfn = 0
    weather_type = 0

    def __init__(self, dfn, wtype):
        self.weather_dfn = dfn
        self.weather_type = wtype

