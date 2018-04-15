#include <SPI.h>
double resistance=74.5;
double voltage=0.0;
int vread=0;
int cs = 4; //chip selector

void setup() {


Serial.begin(9600);
SPI.begin();
pinMode(cs,OUTPUT);
digitalWrite(cs,LOW);
delay(50);

pinMode(9,OUTPUT);
digitalWrite(9,HIGH);
delay(100);
SPI.transfer(0);
delay(1000);
SPI.transfer(1);  
delay(2000);
SPI.end();
}

void loop() {
resistance=128;
for(int i=0; i<=100; i++)
  {   
    SPI.begin();
    SPI.transfer(0);
    SPI.transfer(i);
    delay(1000);
    SPI.end();
  for (int j = 0; j < 100; j++)
  {
    //value=analogRead(1);
    vread = analogRead(A3);
//    Serial.println(value);
    //value=analogRead(1);
    voltage += (double)((double)vread * 5.0) / 1024.0;
  }
  voltage/=100;
  resistance += 18;
  Serial.print("Resistance:"+String(resistance) + " Voltage:");
  Serial.print(voltage,6);
  Serial.print(" Current:");
  Serial.print(voltage / (double)resistance,6);
  Serial.print(" Power:");
  Serial.println((voltage*voltage)/resistance,6);
  
  delay(500);
  
  }

}

