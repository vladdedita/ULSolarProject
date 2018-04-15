/*------------------------------------------------------------------------------------------------------------------------*/
#define ANALOG_PIN A0 // Analog pin
#define RESISTANCE 365// Resistance in ohms


float vOut=0.0;
int panelLength=80;
int panelWidth=100;

double R1=330; // R1 - 
double R2=710; // R2 - 1k ohm



float Area=0.008; //m square
 float Power;
 float Radiation;
 float Voltage;
/*
* Main Setup function
*/
void setup() {
// Begin serial communication

Serial.begin(9600);

}
/*
* Main Setup function
*/
void loop() {
  
vOut=analogRead(ANALOG_PIN);
vOut*=5;
vOut/=1024;
Voltage=vOut/R2*(R1+R2);

Serial.print("Voltage=");
Serial.println(Voltage,4);

Power = pow(Voltage, 2) / RESISTANCE ; // Calculating power
Serial.print("Power=");
Serial.println(Power,4);
Radiation = Power / Area;

//sprintf(msg, " The Solar Radiation is %f W/M2 ", Radiation); // Generating message to be printed 
Serial.print("The Solar Radiation is:");
Serial.print(Radiation,4);
Serial.println(" W/m^2");
delay(3000);
}
/*----------------------------------------------------------------------------------------------------------------------------*/

