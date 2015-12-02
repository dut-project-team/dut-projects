#include "Config.h"
#include "ConfigManager.h"
#include "CfgDef.h"
#include "wserver.h"

int ledPin = 13;  // use the built in LED on pin 13 of the Uno
int state = 0;
int flag = 0;        // make sure that you return the state only once

void setup()
{
    Serial.begin(M_BAUD_RATE);
    setup_bluetooth();
    pinMode(ledPin, OUTPUT);
    digitalWrite(ledPin, LOW);
}

int freeRam () 
{
  extern int __heap_start, *__brkval; 
  int v; 
  return (int) &v - (__brkval == 0 ? (int) &__heap_start : (int) __brkval); 
}
bool ok = false;
void loop()
{
    remote_control();
    delay(1000);
    Serial.println("loop: " + String(freeRam()));
    digitalWrite(ledPin, ok ? HIGH : LOW);
    ok = !ok;
}
