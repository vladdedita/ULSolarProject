
long readVcc() {
  long result;
  // Read 1.1V reference against AVcc
  ADMUX = _BV(REFS0) | _BV(MUX3) | _BV(MUX2) | _BV(MUX1);
  delay(2); // Wait for Vref to settle
  ADCSRA |= _BV(ADSC); // Convert
  while (bit_is_set(ADCSRA,ADSC));
  result = ADCL;
  result |= ADCH<<8;
  result = 1125300L / result; // Back-calculate AVcc in mV
  return result;
}


void setup() {
  Serial.begin(9600);
}

void loop() {
  unsigned int ADCValue;
  double Voltage;
  double Vcc;

  Vcc = readVcc()/1000.0;
  ADCValue = analogRead(0);
  Voltage = (ADCValue / 1024.0) * Vcc;
  Serial.println( Voltage, DEC );
  delay(1000);
}
