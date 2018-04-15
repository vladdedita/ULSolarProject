#include <TheThingsNetwork.h>
#include <Stepper.h>

#include <EEPROM.h>

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
float error = 0.025;
const float defaultError=0.05;

float resistance = 97.5;
int vread = 0;
float voltage = 0;
float measurements[100];
int measurementIndex=0;

int x = 0;

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

unsigned int stepsEast = EEPROMReadInt(69);
unsigned int stepsWest = EEPROMReadInt(96);

unsigned int sensorMinWest= EEPROMReadInt(10);
unsigned int sensorMaxWest= EEPROMReadInt(20);
unsigned int sensorMinEast= EEPROMReadInt(30);
unsigned int sensorMaxEast= EEPROMReadInt(40);

float computeMeasurementAverage(){
  float avg = 0;
  Serial.print("Computing average...");
  for (int i=0; i<measurementIndex; i++){
    avg+=measurements[i];
  }
  avg/=measurementIndex;
  Serial.print("Done (Value =");
  Serial.print(avg,6);
  Serial.println(")");
  return avg;
}
//*****************************************************LORA**************************************************

const char *appEui = "70B3D57ED000A243";
const char *appKey = "7F0C524F8B68B2257AC9B58E076073FB";
#define loraSerial Serial1
// Replace REPLACE_ME with TTN_FP_EU868 or TTN_FP_US915
#define freqPlan TTN_FP_EU868

TheThingsNetwork ttn(loraSerial, Serial, freqPlan);

void ttnConnect(){
  loraSerial.begin(57600); //baud rate 
  // Wait a maximum of 10s for Serial Monitor
  while (!Serial && millis() < 10000);

  Serial.println("-- STATUS");
  ttn.showStatus();

  Serial.println("-- JOIN");
  
  ttn.join(appEui, appKey);
  
}
void ttnSendMeasurements(){
  Serial.println("Sending measurements...");
  byte payload[3];
  float avg = computeMeasurementAverage();  
  unsigned int roundedAvg = round(avg* 10000);
  Serial.print("\t To send:");
  Serial.println(roundedAvg);
  
  payload[0] = highByte(roundedAvg);
  payload[1]= lowByte(roundedAvg);
  //0-West 1-East
  if(stepsWest > stepsEast)
    payload[2]=0;
  else
    payload[2]=1;

  ttn.sendBytes(payload,sizeof(payload));
  
  Serial.println("Done");
}

//*************************************************************************************************************

//*******************************************************TIME******************************************************
typedef struct t  {
    unsigned long tStart;
    unsigned long tTimeout;
}t;

//Tasks and their Schedules.

t t_func1 = {0, 15*60000}; //Run every 60 seconds.

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
//*************************************************************************************************************
//***************************************************MEASUREMENTS**********************************************
void measureVoltage() {
  float voltage=0;
  
  for (int j = 0; j < 100; j++)
      {
        //value=analogRead(1);
        vread = analogRead(A3);
        //    Serial.println(value);
        //value=analogRead(1);
        voltage += (double)((double)vread * 5.0) / 1024.0;
      }
      voltage /= 100;
   if(measurementIndex == 100 )
      measurementIndex = 0;
   measurements[measurementIndex]=(voltage * voltage) / resistance;
   measurementIndex++;
}
float getVoltage() {
  float voltage=0;
  
  for (int j = 0; j < 100; j++)
      {
        //value=analogRead(1);
        vread = analogRead(A3);
        //    Serial.println(value);
        //value=analogRead(1);
        voltage += (double)((double)vread * 5.0) / 1024.0;
      }
      voltage /= 100;
   return voltage;
}
//*************************************************************************************************************
//******************************************************STEPPER************************************************
void moveWest(){
  
    myStepper.setSpeed(stepperSpeed);
    myStepper.step(steps);

    stepsWest += steps;
    stepsEast -= steps;

    delay(300);
    myStepper.setSpeed(0);
    EEPROMWriteInt(69,stepsEast);
    EEPROMWriteInt(96,stepsWest);
}

void moveEast(){
  myStepper.setSpeed(stepperSpeed);
    myStepper.step(-1 * steps);
   
    myStepper.setSpeed(0);
 delay(300);
    stepsWest -= steps;
    stepsEast += steps;
    EEPROMWriteInt(69,stepsEast);
    EEPROMWriteInt(96,stepsWest);
}

struct {
  unsigned int stepsWest;
  unsigned int stepsEast;
  float voltage;  
} st;

void panelCalibration(){
  
  Serial.println("Starting calibration...");
  float voltage=0;
  //st_calibration st[3000]; 
  Serial.print("\t Positioning to east...");
  while(stepsEast != 1600)    moveEast();
  Serial.println("OK");
  Serial.print("\t Going west...");
  st.stepsWest = 0;
  st.stepsEast =0;
  st.voltage=0;
  while(stepsWest != 1600)
  {
    moveWest();  
    delay(200); 
    voltage=getVoltage();
    if(voltage > st.voltage)
    {     
    st.stepsWest=stepsWest;
    st.stepsEast=stepsEast;
    st.voltage=getVoltage();  
    }
      
  }
  Serial.println("OK");
  Serial.print("\t Going east...");
  while(stepsEast !=1600)
  {
    moveEast();  
    delay(200); 
    voltage=getVoltage();     
    if(voltage > st.voltage)
    {     
    st.stepsWest=stepsWest;
    st.stepsEast=stepsEast;
    st.voltage=getVoltage();  
    }
  }
  Serial.println("OK..."); 
  Serial.print("\tPositioning to max position...");
  while(stepsWest != st.stepsWest)   moveWest();
  Serial.println("OK"); 
    
}
//*************************************************************************************************************

void setup() {
  pinMode(pwmA, OUTPUT);
  pinMode(pwmB, OUTPUT);

  pinMode(brakeA, OUTPUT);
  pinMode(brakeB, OUTPUT);

  digitalWrite(brakeA, LOW);
  digitalWrite(brakeB, LOW);  
  
  digitalWrite(pwmA, HIGH);
  digitalWrite(pwmB, HIGH);
  myStepper.setSpeed(0);

  
  // put your setup code here, to run once:
  ttnConnect();
  Serial.begin(9600);  
  
  
  Serial.println("EEPROM stepsEast:" + String(stepsEast));  
  Serial.println("EEPROM stepsWest:" + String(stepsWest));  
  if(EEPROM.read(69) == 0 && EEPROM.read(70) == 0 && EEPROM.read(96) == 0 && EEPROM.read(97) == 0)
  {
      Serial.print("Setting east steps...");
      stepsEast = 1600;
     Serial.println("OK");
     Serial.print("Setting west steps...");
      stepsWest = 0;
     Serial.println("OK");
  }
  delay(50);
  
  
  
  delay(50);
  Serial.println("Entering calibration...");
  delay(50);
  panelCalibration();
  Serial.println("Done");
  tRun(&t_func1);
  
}  
unsigned int readLight(int pin) {
  float sensorValue=0;
  for(int i=0;i<20;i++)
      sensorValueWest+=analogRead(pin);
  sensorValue/=20;
  if(pin == 4){
  sensorValue=map(sensorValue,sensorMinWest,sensorMaxWest,0,255);
  sensorValue=constrain(sensorValue,0,255);
  }
  else
  if(pin==5){
    sensorValue=map(sensorValue,sensorMinEast,sensorMaxEast,0,255);
    sensorValue=constrain(sensorValue,0,255);
  }
  return sensorValue;  
}
void loop() {
 
  // put your main code here, to run repeatedly:
  //x++;
  //  myStepper.step(1600);
  //  myStepper.step(-1500);
  //myStepper.setSpeed(x);
  float westLight;
  westLight = readLight(4);
  float eastLight;
  eastLight= readLight(5);
 
  if (westLight - eastLight > error * westLight && stepsWest != 1600)
  {
    
    moveWest(); 
     
  }
  else if (eastLight - westLight > error * eastLight && stepsEast != 1600)
  {  
   moveEast(); 
  }
  else {
    measureVoltage();    
  }
  if (tCheck(&t_func1)) {
      ttnSendMeasurements();
      tRun(&t_func1);
    }
  
  Serial.print("West:" + String(westLight) + " \tEast:" + String(eastLight) + "\tWest steps:" + String(stepsWest) + "\tEast steps:" + String(stepsEast) + "\t Voltage:" + String(voltage));
 
  delay(100);

}
