int value1;
int value2;
int value3;
double v1;
double v2;
double v3;
int direction=HIGH;

#define B_BRAKE 8
#define B_DIR 13
#define B_SPEED 11


#define A_BRAKE 9
#define A_DIR 12
#define A_SPEED 3

#include <Servo.h>


void setup() {
  
  //Setup Channel A
 
//  
//  pinMode(12, OUTPUT); //Initiates Motor Channel A pin
//  pinMode(9, OUTPUT); //Initiates Brake Channel A pin

   
   Serial.begin(9600);
}

void loop(){

 
   digitalWrite(A_DIR, LOW);
   
  digitalWrite(A_BRAKE, LOW);
  digitalWrite(A_SPEED,255);
  delay(1000);
  digitalWrite(A_BRAKE,HIGH);
  delay(5000);

  
 // delay(5000);
  }
  
  

