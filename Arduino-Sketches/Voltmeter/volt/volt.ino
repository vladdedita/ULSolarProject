
#include <SPI.h>
int analogInput = 0;
double vOut = 0.0;
double vIn = 0.0;

double Rt = 97.5;
int value = 0;
double voltage = 0.0;
double amps = 0.0;
int power = 1;


void setup() {
  Serial.begin(9600);
  
 
  //pinMode(0,INPUT);

}
int i=0;
void loop() {  
  // put your main code here, to run repeatedly:
  for (int i = 0; i < 200; i++)
  {
    //value=analogRead(1);
    value = analogRead(A3);
//    Serial.println(value);
    //value=analogRead(1);
    vOut += (double)((double)value * 5.0) / 1024.0;
  }
  vOut/=200;
  
//  if (voltage < 0.1)
//    voltage = 0;

  //vIn=vOut / (R2/(R1+R2));
  //amps=power/vIn;

//  amps = vOut / Rt;

  Serial.print("V=");
  Serial.println(vOut, 4);
//  Serial.print(" A=");
//  Serial.print(amps, 4);
//  Serial.print("P=");
//  Serial.println( (vOut * vOut) / Rt , 4);
//  delay(1000);
//Serial.println(analogRead(A3));

}
