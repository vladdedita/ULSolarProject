#include <TheThingsNetwork.h>
#include <Stepper.h>
#include <EEPROM.h>

//***************************************************Update Time period
long int ttnUpdatePeriod = 15; //minutes
long int voltageReadPeriod = (ttnUpdatePeriod * 60000) / 100; //100 samples in ttnUpdatePeriod minutes
long int solarTrackingPeriod = 60000; //1 minute
//**************************************************************

//***************************************************LoRa Init

const char *appEui = "70B3D57ED000A243";
const char *appKey = "7F0C524F8B68B2257AC9B58E076073FB";


#define loraSerial Serial1
// Replace REPLACE_ME with TTN_FP_EU868 or TTN_FP_US915
#define freqPlan TTN_FP_EU868

TheThingsNetwork ttn(loraSerial, Serial, freqPlan);

//**************************************************************


//***************************************************Stepper Init
const int stepsPerRevolution = 4096;
const int dirA = 12;
const int dirB = 13;
//albs v g r
Stepper myStepper(stepsPerRevolution, dirA, dirB);

const int pwmA = 3;
const int pwmB = 11;
const int brakeA = 9;
const int brakeB = 8;
const int steps = 20;
const int stepperSpeed = 3;

//**************************************************************


//***************************************************LDR Init
float error = 10;
const float defaultError = 0.05;

//***************************************************Voltage measurement Init
float resistance = 97.5;
int vread = 0;
float voltage = 0;

float measurements[100];
int measurementIndex = 0;

int x = 0;

//**************************************************************



//******************************************************BOARD***************************************************************
void(* resetFunc) (void) = 0;

void EEPROMWriteInt(int p_address, int p_value)
{
  byte lowByte = ((p_value >> 0) & 0xFF);
  byte highByte = ((p_value >> 8) & 0xFF);

  EEPROM.write(p_address, lowByte);
  EEPROM.write(p_address + 1, highByte);
}

//This function will read a 2 byte integer from the eeprom at the specified address and address + 1
unsigned int EEPROMReadInt(int p_address)
{
  byte lowByte = EEPROM.read(p_address);
  byte highByte = EEPROM.read(p_address + 1);

  return ((lowByte << 0) & 0xFF) + ((highByte << 8) & 0xFF00);
}
//*********************************************************************************************************************
unsigned int stepsEast = EEPROMReadInt(69);
unsigned int stepsWest = EEPROMReadInt(96);

unsigned int sensorMinWest = EEPROMReadInt(10);
unsigned int sensorMaxWest = EEPROMReadInt(20);
unsigned int sensorMinEast = EEPROMReadInt(30);
unsigned int sensorMaxEast = EEPROMReadInt(40);

float computeMeasurementAverage() {
  float avg = 0;
  Serial.print("Computing average...");
  for (int i = 0; i < measurementIndex; i++) {
    avg += measurements[i];
  }
  avg /= measurementIndex;
  Serial.print("Done (Value =");
  Serial.print(avg, 6);
  Serial.println(")");
  return avg;
}
//*******************************************************TIME******************************************************

typedef struct t  {
  unsigned long tStart;
  unsigned long tTimeout;
} t;

//Tasks and their Schedules.
t t_func1 = {0, ttnUpdatePeriod * 60000}; //Run every ttnUpdatePeriod minutes.
t t_func2= {0, voltageReadPeriod}; //Run every voltagereadPeriod minutes
t t_func3 = {0, solarTrackingPeriod}; //Run every minute

bool tCheck (struct t *t ) {
  if (millis() > t->tStart + t->tTimeout)
  {
    return true;
  }
  return false;
}

void tRun (struct t *t) {
  t->tStart = millis();
}
//***********************************************************************************************************

//*****************************************************LORA**************************************************

void ttnConnect() {
  loraSerial.begin(57600);
  Serial.begin(9600);
  
  // Wait a maximum of 10s for Serial Monitor
  Serial.println("-- STATUS");
  ttn.showStatus();   
  Serial.println("-- JOIN");
  ttn.join(appEui, appKey);
  ttn.onMessage(message);
}

void ttnSendMeasurements() {
  Serial.println("Sending measurements...");
  byte payload[3];
  float avg = computeMeasurementAverage();
  unsigned int roundedAvg = round(avg * 10000);
  Serial.print("\t To send:");
  Serial.println(roundedAvg);

  payload[0] = highByte(roundedAvg);
  payload[1] = lowByte(roundedAvg);
  //0-West 1-East
  if (stepsWest > stepsEast)
    payload[2] = 0;
  else
    payload[2] = 1;

  ttn.sendBytes(payload, sizeof(payload));
  measurementIndex = 0;
  Serial.println("Done");
}
void message(const uint8_t *payload, size_t size, port_t port)
{

  Serial.println("-- MESSAGE");
  Serial.print("Received " + String(size) + " bytes on port " + String(port) + ":");

  if (port == 2)
  {
    Serial.println("Changing update time to: ");
    ttnUpdatePeriod = (payload[0] << 8) + payload[1];
    (&t_func1)->tTimeout = ttnUpdatePeriod * 60000;
    Serial.println(ttnUpdatePeriod);
  }
  if(port==3)
  {
    if(payload[0] == 0)
    {
      //going EAST
      gotoEast();
    }
    else
    if(payload[0]==1)
    {
      gotoWest();
    }
  }
  if(port==4){
    if(payload[0] == 0){
      //reset
      resetFunc();
    }
    else
    if(payload[0] == 1){
      //sleep
    }
    else
    if(payload[0]==2){
      panelCalibration()    ;
    }
  }
  Serial.println("--END");

  Serial.println();
}


//*************************************************************************************************************


//***************************************************MEASUREMENTS**********************************************
void measureVoltage() {
  float voltage = 0;
  for (int j = 0; j < 100; j++)
  {
    //value=analogRead(1);
    vread = analogRead(A3);
    //    Serial.println(value);
    //value=analogRead(1);
    voltage += (double)((double)vread * 5.0) / 1024.0;
  }
  voltage /= 100;
  if (measurementIndex == 100)
      return;
  measurements[measurementIndex] = (voltage * voltage) / resistance;
  measurementIndex++;
}

unsigned int readLight(int pin) {
  float sensorValue = 0;
  for (int i = 0; i < 20; i++)
    sensorValue += analogRead(pin);
  sensorValue /= 20;
  if (pin == 4) {
    sensorValue = map(sensorValue, sensorMinWest, sensorMaxWest, 0, 255);
    sensorValue = constrain(sensorValue, 0, 255);
  }
  else if (pin == 5) {
    sensorValue = map(sensorValue, sensorMinEast, sensorMaxEast, 0, 255);
    sensorValue = constrain(sensorValue, 0, 255);
  }
  delay(50);
  return sensorValue;
}
//*************************************************************************************************************


//******************************************************SOLAR TRACKING************************************************
void moveWest() {

  myStepper.setSpeed(stepperSpeed);
  myStepper.step(steps);

  stepsWest += steps;
  stepsEast -= steps;
 
  myStepper.setSpeed(0);
  EEPROMWriteInt(69, stepsEast);
  EEPROMWriteInt(96, stepsWest);

  delay(100);
}

void moveEast() {
  myStepper.setSpeed(stepperSpeed);
  myStepper.step(-1 * steps);
  myStepper.setSpeed(0);  
  stepsWest -= steps;
  stepsEast += steps;
  EEPROMWriteInt(69, stepsEast);
  EEPROMWriteInt(96, stepsWest);
  delay(100);
}

struct {
  unsigned int stepsWest;
  unsigned int stepsEast;
  int error;
} st;

void panelCalibration() {

  digitalWrite(5,HIGH);
   delay(100);
  Serial.println("Starting calibration...");
  int error = 0;
  //st_calibration st[3000];
  Serial.print("\t Positioning to east...");
  while (stepsEast != 1600)    moveEast();
  Serial.println("OK");
  Serial.print("\t Going west...");
  st.stepsWest = 0;
  st.stepsEast = 0;
  st.error = 500;
  while (stepsWest != 1600)
  {
    moveWest();   
    error = abs(readLight(4) - readLight(5));
    if (error < st.error)
    {
      st.stepsWest = stepsWest;
      st.stepsEast = stepsEast;
      st.error = error;
    }

  }
  Serial.println("OK");
  
  Serial.print("\t Going east...");
  while (stepsEast != 1600)
  {
    moveEast();    
    error = abs(readLight(4) - readLight(5));
    if (error < st.error)
    {
      st.stepsWest = stepsWest;
      st.stepsEast = stepsEast;
      st.error = error;
    }
  }
  Serial.println("OK...");
  Serial.print("\tPositioning to max position...");
  while (stepsWest != st.stepsWest)   moveWest();

  digitalWrite(5,LOW);
  Serial.println("OK");

}

void gotoWest() {
  while(stepsWest != 1600)
    moveWest();
}
void gotoEast() {
  while(stepsEast != 1600)
    moveEast();
}

void trackSun(){ 
  digitalWrite(5,HIGH);
  delay(100);
  float westLight;
  float eastLight;
   westLight = readLight(4);
    eastLight = readLight(5);
  while( abs(westLight - eastLight) > error && stepsWest!=1600 && stepsEast !=1600)
  {    
    westLight = readLight(4);
    eastLight = readLight(5);
    if (westLight - eastLight > error  && stepsWest != 1600)
    {
      moveWest();
    }
    else if (eastLight - westLight > error  && stepsEast != 1600)
    {
      moveEast();
    }
    Serial.println("West:" + String(westLight) + " \tEast:" + String(eastLight) + "\tWest steps:" + String(stepsWest) + "\tEast steps:" + String(stepsEast));
  }
  digitalWrite(5,LOW);
}
//*************************************************************************************************************

void setup() {
  Serial.begin(9600);
  pinMode(pwmA, OUTPUT);
  pinMode(pwmB, OUTPUT);
  pinMode(brakeA, OUTPUT);
  pinMode(brakeB, OUTPUT);
  pinMode(5,OUTPUT);
  digitalWrite(brakeA, LOW);
  digitalWrite(brakeB, LOW);
  digitalWrite(pwmA, HIGH);
  digitalWrite(pwmB, HIGH); 
  myStepper.setSpeed(0);
  
  Serial.println("EEPROM stepsEast:" + String(stepsEast));
  Serial.println("EEPROM stepsWest:" + String(stepsWest));
  if (EEPROM.read(69) == 0 && EEPROM.read(70) == 0 && EEPROM.read(96) == 0 && EEPROM.read(97) == 0)
  {
    Serial.print("Setting east steps...");
    stepsEast = 1600;
    Serial.println("OK");
    Serial.print("Setting west steps...");
    stepsWest = 0;
    Serial.println("OK");
  }
  Serial.println("Entering calibration...");
  delay(50);
  //panelCalibration();
  Serial.println("Done");

  ttnConnect();
  tRun(&t_func1);
  tRun(&t_func2);
  panelCalibration();
  tRun(&t_func3);
}

void loop() {  
//  if (tCheck(&t_func3)) {//SOLAR TRACKING
    trackSun();
//    tRun(&t_func3);
//  }
  
  if (tCheck(&t_func2)) {//VOLTAGE
    measureVoltage();
    tRun(&t_func2);
  }
  
  if (tCheck(&t_func1)) { //LORA
    ttnSendMeasurements();
    tRun(&t_func1);
  }
  

//  Serial.println("West:" + String(westLight) + " \tEast:" + String(eastLight) + "\tWest steps:" + String(stepsWest) + "\tEast steps:" + String(stepsEast) + "\t Voltage:" + String(voltage));

  delay(60000);

}
