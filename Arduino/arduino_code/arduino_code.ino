
#include <LiquidCrystal.h>

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);        // select the pins used on the LCD panel

unsigned long tepTimer;

int hour = 0;
int hourTime = 4000;
int actualTime = 0;
int lastTime = 0;

struct _arduino_data{
  int hour;
  int day;
  int currentWeatherId;
  int nextWeatherId;
};

struct _meteo{
  int probability;
  char meteo_name[10];
};

bool isANewDay = false;

typedef struct _meteo meteo;
typedef struct _arduino_data arduino_data;

meteo meteos[5];

arduino_data data;

int getRandomNeighborWeather(int id){
   int decalage = (random(2))*2-1;
   id += decalage;
   if(id > 4)
    id = 0;
   if(id < 0)
    id = 4;

    return id;
}

int getRandomWeatherIdFromStatsWhereNot(int id){
    return random(5);
}


int getRandomMeteoFromStats(){
   int proba = random(100);
   int i = -1;
   while(proba > 0){
      i++;
      proba -= meteos[i].probability;
   }

   return i;
}

void setup() {
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
  data.hour = 0;
  data.day = 0;
  meteos[0].probability = 5;
  strcpy(meteos[0].meteo_name, "Orage");
  meteos[1].probability = 15;
  strcpy(meteos[1].meteo_name, "Pluie");
  meteos[2].probability = 20;
  strcpy(meteos[2].meteo_name, "Nuage");
  meteos[3].probability = 40;
  strcpy(meteos[3].meteo_name, "Soleil");
  meteos[4].probability = 20;
  strcpy(meteos[4].meteo_name, "Canicule");
  data.currentWeatherId = getRandomMeteoFromStats();
  data.nextWeatherId = getRandomMeteoFromStats();
  lcd.begin(16, 2); 
  
}


int getHourTime(int actualTime){
  return actualTime/hourTime;
}

int removeHour(int actualTime){
    return actualTime%hourTime;
}


void sendData(){
  actualTime += (millis() - lastTime);
  lastTime = millis();
  int lastHour = data.hour;
  
  data.hour += getHourTime(actualTime);
  actualTime = removeHour(actualTime);
  if(data.hour > 23)
   {
      isANewDay = true;
      data.hour = 0;
      data.day++;
   }
   else{
    isANewDay = false;
   }
  if(data.hour != lastHour){
    char data_send[255];
    sprintf(data_send, "@%01d;%01d;%01d!", data.hour + data.day*24, data.currentWeatherId, data.nextWeatherId);
    Serial.write(data_send, strlen(data_send));
  }
  
}

void updateMeteo(){
  if(isANewDay){
      int prevision_correct = random(100);
      if(prevision_correct < 88){
         data.currentWeatherId = data.nextWeatherId;
      }
      else if(prevision_correct < 98){
        data.currentWeatherId = getRandomNeighborWeather(data.nextWeatherId);
      }
      else if(prevision_correct < 100){
        data.currentWeatherId = getRandomWeatherIdFromStatsWhereNot(data.nextWeatherId);
      }
      data.nextWeatherId = getRandomMeteoFromStats();
  }
}

void loop() {
  sendData();
  updateMeteo();
  hour++;
  lcd.setCursor(0, 0);
  if(millis() - tepTimer > 500){
      tepTimer = millis();
      lcd.print("Il est : ");
      lcd.print(data.hour);
      lcd.print(" h  ");
      lcd.setCursor(0, 30);
      char meteo_display[64] = "";
      sprintf(meteo_display, "%s -> %s       ", meteos[data.currentWeatherId].meteo_name,meteos[data.nextWeatherId].meteo_name);
      lcd.print(meteo_display);
    } 
}
