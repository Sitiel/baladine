
#include <LiquidCrystal.h>

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);        // select the pins used on the LCD panel

unsigned long tepTimer;
unsigned long buttonTimer;

int hourTime = 12500;
int buttonTime = 200;
int actualTime = 0;
int lastTime = 0;

struct _arduino_data {
  int hour;
  int day;
  int currentWeatherId;
  int nextWeatherId;
};

struct _meteo {
  int probability;
  char meteo_name[10];
};

bool isANewDay = false;
bool button = true;

typedef struct _meteo meteo;
typedef struct _arduino_data arduino_data;
bool draw = false;

#define btnRIGHT  0
#define btnUP     1
#define btnDOWN   2
#define btnLEFT   3
#define btnSELECT 4
#define btnNONE   5

meteo meteos[5];

arduino_data data, nextData;


int getRandomNeighborWeather(int id) {
  int decalage = (random(2)) * 2 - 1;
  id += decalage;
  if (id > 4)
    id = 0;
  if (id < 0)
    id = 4;

  return id;
}

int getRandomWeatherIdFromStatsWhereNot(int id) {
  return random(5);
}


int getRandomMeteoFromStats() {
  int proba = random(100);
  int i = -1;
  while (proba > 0) {
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
  buttonTimer = 0;
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


int getHourTime(int actualTime) {
  return actualTime / hourTime;
}

int removeHour(int actualTime) {
  return actualTime % hourTime;
}


void updateHour() {
  actualTime += (millis() - lastTime);
  lastTime = millis();

  nextData.day = data.day;
  nextData.hour = data.hour + getHourTime(actualTime);


  actualTime = removeHour(actualTime);
  if (nextData.hour > 23)
  {
    isANewDay = true;
    nextData.hour = 0;
    nextData.day++;
  }
  if (data.hour != nextData.hour) {
    char data_send[255];
    sprintf(data_send, "@%01d;%01d;%01d!", nextData.hour + nextData.day * 24, nextData.currentWeatherId, nextData.nextWeatherId);
    Serial.write(data_send, strlen(data_send));
  }

}

void outputHour() {
  isANewDay = false;
  data.day = nextData.day;
  data.hour = nextData.hour;
}

void displayHour() {
  if (draw) {
    lcd.setCursor(0, 0);
    lcd.print(data.hour);
    lcd.print("h  ");
    char speedDisplay[64] = "";
    sprintf(speedDisplay, "%dms            ", hourTime);
    lcd.print(speedDisplay);
    
  }
}

void updateMeteo() {

  nextData.currentWeatherId = data.currentWeatherId;
  nextData.nextWeatherId = data.nextWeatherId;
  if (isANewDay) {
    int prevision_correct = random(100);
    if (prevision_correct < 88) {
      nextData.currentWeatherId = nextData.nextWeatherId;
    }
    else if (prevision_correct < 98) {
      nextData.currentWeatherId = getRandomNeighborWeather(nextData.nextWeatherId);
    }
    else if (prevision_correct < 100) {
      nextData.currentWeatherId = getRandomWeatherIdFromStatsWhereNot(nextData.nextWeatherId);
    }
    nextData.nextWeatherId = getRandomMeteoFromStats();
  }
}


void outputMeteo() {
  data.currentWeatherId = nextData.currentWeatherId;
  data.nextWeatherId = nextData.nextWeatherId;

}




void displayMeteo() {
  if (draw) {
    lcd.setCursor(0, 1);
    char meteo_display[64] = "";
    sprintf(meteo_display, "%s -> %s       ", meteos[nextData.currentWeatherId].meteo_name, meteos[nextData.nextWeatherId].meteo_name);
    lcd.print(meteo_display);
  }
}

void updateLCD() {
  if (millis() - tepTimer > 500) {
    tepTimer = millis();
    draw = true;
  }
}

void updateButton() {
  if(millis() - buttonTimer > buttonTime){
      buttonTimer = millis();
      button = true;
  }
}

void outputButton(){
  if(button){
    button = false;
    int lcd_key = read_LCD_buttons();
      switch (lcd_key) {
        case btnRIGHT: {
          if(hourTime < 30000)
            hourTime+=100;
            break;
          }
        case btnLEFT: {
            if (hourTime > 100)
              hourTime-=100;
            break;
          }
        case btnUP: {
            break;
          }
        case btnDOWN: {
            break;
          }
        case btnSELECT: {
            break;
          }
        case btnNONE: {
          button = true;
            break;
          }
      }
  }
  
}

void outputLCD() {
  draw = false;
}

int read_LCD_buttons() {
  int adc_key_in = analogRead(0);

  if (adc_key_in > 1000) return btnNONE;

  if (adc_key_in < 50)   return btnRIGHT;
  if (adc_key_in < 250)  return btnUP;
  if (adc_key_in < 450)  return btnDOWN;
  if (adc_key_in < 650)  return btnLEFT;
  if (adc_key_in < 850)  return btnSELECT;

  return btnNONE;
}

void loop() {

  updateButton();
  updateHour();
  updateMeteo();
  updateLCD();

  outputHour();
  outputMeteo();
  outputButton();

  displayHour();
  displayMeteo();

  outputLCD();
}
