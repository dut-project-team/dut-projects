#include "Sensor.h"
#include <Arduino.h>

byte_t getLightSensor(ubyte id)
{
    return digitalRead(id) == HIGH ? 5 : 100;
}

ubyte getPeopleSensor(ubyte id)
{
    return digitalRead(id) == HIGH ? 1 : 0;
}
