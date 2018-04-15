#define SENSOR_PIN A0
#define SENS 0.185
#define ROUNDS 10
#define SAMPLING 50
double analogReadOffset = 0.0;
double voltageOffset = 0.0;
double currentOffset = 0.0;
double adcOffset = 0.0;

double readSensor();
double estimateCurrent();
double computeCurrentAverage();
int notCalibrating = 0;




double computeVoltageAverage() {
  double value = 0.0;
  for (int i = 0; i < SAMPLING; i++)
    value += readSensor();
  value /= SAMPLING;
  value /= 1024;
  value *= 5;
  value -= voltageOffset;
  //Serial.println("Voltage calibration:" + String(value) + " Offset:" + String(voltageOffset));
  return value;

}
void voltageCalibration() {
  static int rounds = 0;
  double calibrationAverage = 0.0;
  if (rounds < ROUNDS)
  {
    voltageOffset += computeVoltageAverage();
    calibrationAverage += voltageOffset;
    rounds++;
    voltageCalibration();
  }
}
double computeCurrentAverage()
{
  double value = 0.0;
  for (int i = 0; i < SAMPLING * 2; i++)
  {
    value += estimateCurrent();
  }
  value /= SAMPLING * 2;
  //  Serial.println("Current calibration:");
  //  Serial.print(value,5);
  //  Serial.print("\tOffset:");
  //  Serial.println(currentOffset);
  return value;
}

void currentCalibration() {
  static int rounds = 0;
  if (rounds < ROUNDS)
  {
    currentOffset += computeCurrentAverage();
    rounds++;
    currentCalibration();
  }
}

void calibration() {

  Serial.println("Starting Voltage Calibration...");
  voltageCalibration();
  Serial.println("OK");
  Serial.println("Starting Current Calibration...");
  currentCalibration();
  Serial.println("OK");

}





void setup() {

  Serial.begin(9600);
    Serial.println("Starting calibration...");
    calibration();
    Serial.println("Start...");
    pinMode(0,INPUT);

  calibration();
  pinMode(9, OUTPUT);
  digitalWrite(9, HIGH);
  pinMode(8, OUTPUT);
  digitalWrite(8, HIGH);
  delay(2000);

    Serial.println("Done");
    Serial.println("Entering loop...");

}
double x[10];
int counter = 0;



double vectorAverage() {
  double avg = 0.0;
  for (int i = 0; i < 10; i++)
    avg += x[i];
  return avg / 10;
}
int closestToAverage(double avg) {
  int i = 0;
  double minDif = 999;
  int index;
  for (i = 0; i < 10; i++)
  {
    if ( abs(x[i] - avg) < minDif)
    {
      minDif = abs(x[i] - avg);
      index = i;
    }
  }
  return index;
}
void sort()
{
  double aux;
  for (int i = 0; i < 10; i++)
  {
    for (int j = i + 1; j < 10; j++)
    {
      if (x[i] < x[j])
      {
        aux = x[i];
        x[i] = x[j];
        x[j] = aux;
      }
    }
  }
}
void loop() {

  x[counter] = computeCurrentAverage() * 1000;
  Serial.println("x[" + String(counter) + "]=" + x[counter]);
  counter++;
  if (counter == 10)
  {
    counter = 0;
    sort();
    double current = x[5];
    Serial.println();
    Serial.print("Current: ");
    Serial.println(current, 5);
  }
}


double readSensor() {

  double value;
  uint32_t sensorValue = 0;
  for (int i = 0; i < SAMPLING; i++)
  {
    sensorValue += analogRead(SENSOR_PIN);
    delay(1);
  }
  value = (double)((double)sensorValue / SAMPLING);
  //   Serial.print("\tAnalog read:");
  //   Serial.print(sensorValue);
  return value;
}
double measureVoltage() {
  double value = 0.0;
  value = readSensor();
  //  Serial.print("\tAnalog read:" + String(value));
  value = (double)(( (double)value / 1024.0) * 5); // V
  //  Serial.print("\t\tVoltage:");
  //  Serial.print(value - voltageOffset, 5); // V
  //  Serial.print("V");

  return value;
}
double estimateCurrent() {
  double value;
  double voltage;
  voltage = measureVoltage();
//  Serial.print("\t\tVoltage:");
//  Serial.print(voltage, 5); // V
//  Serial.print("\t\tOffset Voltage:");
//  Serial.print(voltageOffset, 5); // V
//  Serial.println("V");
  if (voltage > voltageOffset)
  { value = ( (voltage - voltageOffset) / (double)SENS );
    if ( value - currentOffset < 0 )
    {
      value = 0;
    }
    else
    {
      value -= currentOffset;
    }
  }
  else
  {
    value = 0;
  }

//  Serial.print("\t\t\tCurrent:");
//  Serial.print(value, 5);
//  Serial.println("A");
  return value;
}






