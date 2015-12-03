#include <Wire.h>
#include <Arduino.h>
#include <SoftwareSerial.h>
#include <EEPROM.h>
#include "Config.h"
#include "ConfigManager.h"
#include "CfgDef.h"
#include "wserver.h"
#include "Monitor.h"

#ifdef DEBUG_MODE
const int loop_check_pin = 13;
bool loop_check_pin_state = true;
uint _currentTime = 0;
ubyte hour = 0;

extern void log(String st);

void loadSample()
{
    UserConfig* uc1 = new UserConfig();
    uc1->n_configs = M_CONFIG_MODE_DEF;

    /*
    uint lstTime1[] = {m_timeint(5, 0), m_timeint(8, 0), m_timeint(11, 0), 0, 0, 0, 0, 0, 0, 0};
    memcpy(uc1->lstTime, lstTime1, sizeof(lstTime1));

    ubyte listConfig1[] = {1, 0, 1, 0, 0, 0, 0, 0, 0, 0};
    memcpy(uc1->lstConfig, listConfig1, sizeof(listConfig1));
    */

    strcpy(uc1->name,"light 1");
    uc1->extra = to_extra(M_LIGHT_LIVINGROOM, 12);
    uc1->sensor = to_sensor(6, 8);
    log("Sensor: " + String(uc1->sensor));

    addUserConfig(uc1);

    delete uc1;
}

int freeRam ()
{
    extern int __heap_start, *__brkval;
    int v;
    return (int) &v - (__brkval == 0 ? (int) &__heap_start : (int) __brkval);
}

void checkloop()
{
    log("loop: " + String(freeRam()));
    digitalWrite(loop_check_pin, loop_check_pin_state ? HIGH : LOW);
    loop_check_pin_state = !loop_check_pin_state;
}

uint currentTime()
{
    return _currentTime;
}

void updateTime()
{
    ++hour;
    _currentTime = m_timeint(hour, 0);
    if (hour >= 24)
    {
        hour = 0;
        _currentTime = 0;
    }
    if (getTimeType() == M_TIME_DAYTIME)
        log("---+Hour: " + String((int)hour));
    else
        log("---~Hour: " + String((int)hour));
}
#endif // DEBUG_MODE

void setup()
{
#ifdef DEBUG_MODE
    Serial.begin(M_BAUD_RATE);
#endif // DEBUG_MODE
    setup_bluetooth();

    /** note! reset all configs by clear user config and set default value for def config */
    initConfigs();
#ifdef DEBUG_MODE
    loadSample();
#endif // DEBUG_MODE
    setupLights();
    setupSensors();
}

void loop()
{
#ifdef DEBUG_MODE
    updateTime();
    checkloop();
#endif // DEBUG_MODE
    applyConfigs();
    remote_control();
    delay(1000);
}
