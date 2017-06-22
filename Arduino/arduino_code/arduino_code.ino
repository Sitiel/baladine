
int hour = 0;
int hourTime = 100;
int actualTime = 0;
int lastTime = 0;

struct _arduino_data{
  int hour;
  int day;
  int currentWeatherId;
  int nextWeatherId;
};

typedef struct _arduino_data arduino_data;

arduino_data data;

void setup() {
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
  data.hour = 0;
  data.day = 0;
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
      data.hour = 0;
      data.day++;
   }
  data.currentWeatherId = 0;
  data.nextWeatherId = 0;
  if(data.hour != lastHour){
    char data_send[255];
    sprintf(data_send, "@%01d;%01d;%01d!", data.hour, data.currentWeatherId, data.nextWeatherId);
    Serial.write(data_send, strlen(data_send));
  }
  
}


void loop() {
  sendData();
  hour++;

}
