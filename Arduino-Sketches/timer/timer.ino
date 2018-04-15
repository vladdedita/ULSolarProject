
unsigned long startTime;
unsigned long timer = 2000; //2 seconds

void startTimer() {
  startTime=millis();
}
boolean checkTime(){
  
  if(millis() - startTime > timer){
    return true;
  }
  return false;
}

void setup() { 
  Serial.begin(9600);
  startTimer();
}

void loop() {
  if(checkTime () )
  {
    Serial.println("A trecut");
    startTimer();
  }
  
}
