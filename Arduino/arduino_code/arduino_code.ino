
int hour = 0;

struct _arduino_data{
  int hour;
  char currentWeatherId;
  char nextWeatherId;
};

typedef struct _arduino_data arduino_data;

void setup() {
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
}


void sendData(){
  arduino_data data;
  data.hour = hour;
  data.currentWeatherId = 0;
  data.nextWeatherId = 0;
  char data_send[255];
  sprintf(data_send, "@%d;%d;%d;", data.hour, data.currentWeatherId, data.nextWeatherId);
  Serial.write(data_send, strlen(data_send));
  delay(300);
}


void loop() {
  sendData();
  hour++;

}
