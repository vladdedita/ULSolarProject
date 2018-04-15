#include<EEPROM.h>

unsigned int sensorMinEast=1023; //minimum High

unsigned int sensorMaxEast=0; //maximum Low

unsigned int sensorValueEast=0;


unsigned int sensorMinWest=1023; //minimum High

unsigned int sensorMaxWest=0; //maximum Low
 
unsigned int sensorValueWest=0;

 void EEPROMWriteInt(int p_address, int p_value)
     {
     byte lowByte = ((p_value >> 0) & 0xFF);
     byte highByte = ((p_value >> 8) & 0xFF);

     EEPROM.write(p_address, lowByte);
     EEPROM.write(p_address + 1, highByte);
     }
     
void setup() {
  // put your setup code here, to run once: 
  delay(1000);
  pinMode(5,OUTPUT);
  digitalWrite(5,HIGH);
  
 Serial.begin(9600);
  Serial.println("Begin calibration west...");
  while(Serial.available() <= 0) {
    sensorValueWest=analogRead(A4);
    //record the maximum sensor value
    if(sensorValueWest > sensorMaxWest)
      sensorMaxWest=sensorValueWest;
    //record the minimum sensor value
    if(sensorValueWest < sensorMinWest)
      sensorMinWest=sensorValueWest;      
  }
  Serial.read();
  Serial.println("Done");
  Serial.println("Begin calibration east...");
  while(Serial.available() <=0) {
    sensorValueEast=analogRead(A5);
    //record the maximum sensor value
    if(sensorValueEast > sensorMaxEast)
      sensorMaxEast=sensorValueEast;
    //record the minimum sensor value
    if(sensorValueEast < sensorMinEast)
      sensorMinEast=sensorValueEast;      
  }
  Serial.println("Done");

  EEPROMWriteInt(10,sensorMinWest);
    
  EEPROMWriteInt(20,sensorMaxWest);

  EEPROMWriteInt(30,sensorMinEast);
    
  EEPROMWriteInt(40,sensorMaxEast);

  
  
}

void loop() {
     
     
    sensorValueWest=0;
    sensorValueEast=0;

    for(int i=0;i<20;i++)
      sensorValueWest+=analogRead(A4);
      
    sensorValueWest/=20;
    Serial.print("AnW:" + String(sensorValueWest));    
    sensorValueWest=map(sensorValueWest,sensorMinWest,sensorMaxWest,0,255);
    sensorValueWest=constrain(sensorValueWest,0,255);
    Serial.print("\tMapped West: ");
    Serial.print(sensorValueWest);
    
 for(int i=0;i<20;i++)
      sensorValueEast+=analogRead(A5);

    sensorValueEast/=20;   
   
     Serial.print("AnE:" + String(sensorValueEast));    
    sensorValueEast=map(sensorValueEast,sensorMinEast,sensorMaxEast,0,255);
    sensorValueEast=constrain(sensorValueEast,0,255);    
    Serial.print("\t East: ");
    Serial.println(sensorValueEast);
}
