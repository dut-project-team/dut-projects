#include "Sensor.h"
#include <Arduino.h>

byte_t getLightSensor(ubyte pin)
{
    return digitalRead(pin) == HIGH ? 0 : 100;
}

ubyte getPeopleSensor(ubyte pin)
{
    return digitalRead(pin) == HIGH ? 1 : 0;
}
