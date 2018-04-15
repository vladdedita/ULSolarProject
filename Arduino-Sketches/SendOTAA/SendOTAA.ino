#include <TheThingsNetwork.h>

// Set your AppEUI and AppKey
const char *appEui = "70B3D57ED000A243";
const char *appKey = "7F0C524F8B68B2257AC9B58E076073FB";
      
      
      boolean flag=false;
      byte data[2];
      String temp;
      
#define loraSerial Serial1
#define debugSerial Serial

// Replace REPLACE_ME with TTN_FP_EU868 or TTN_FP_US915
#define freqPlan TTN_FP_EU868

TheThingsNetwork ttn(loraSerial, debugSerial, freqPlan);

void setup()
{
  loraSerial.begin(57600); //baud rate
  debugSerial.begin(9600);

  // Wait a maximum of 10s for Serial Monitor
  while (!debugSerial && millis() < 10000);

  debugSerial.println("-- STATUS");
  ttn.showStatus();

  debugSerial.println("-- JOIN");
  
  ttn.join(appEui, appKey);

 
}
int i=0;
void loop()
{

//    Serial.println("Enter data to send:");
//    
//    while(flag==false)
//    {
//      
//      
//
//      temp=Serial.readString();
//
//      
//
//      data[data.length()]=temp;
//     
//    }
//  
//      
//  data=(byte*)malloc((temp.length()+1) * sizeof(byte));
// 
//  temp.getBytes(data,temp.length());
//  

//  i++;
//  data[0]=highByte(i);
//  data[1]=lowByte(i);
//  ttn.sendBytes(data, sizeof(data));
//  
  
  

  //ttn.sendBytes()
  
}
