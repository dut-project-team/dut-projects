#include "Monitor.h"
#include "Sensor.h"
#include <Arduino.h>
#include <SoftwareSerial.h>
#include "CfgDef.h"
#ifndef DEBUG_MODE
#include "ds1307.h"
#endif // DEBUG_MODE

uint __lights_state = 0;// left <- right

#ifdef DEBUG_MODE
extern uint currentTime();
#endif // DEBUG_MODE

uint getCurrentTime()
{
#ifdef DEBUG_MODE
    return currentTime();// just for example
#else
    return getTime();
#endif // DEBUG_MODE
}

uint getRelativelyTime(uint time)
{
    return (time >= 1080) ? time : time + 1440;
}

bool isOnTime(uint intTime)
{
    uint time = getCurrentTime();
    return isOnTime(time, intTime);
}

bool isOnTime(uint currentTime, uint intTime)
{
    return (intTime >= currentTime) && (intTime < currentTime + 3);
}

ubyte getTimeType()
{
    uint time = getCurrentTime();
    // 6h to 18h is day time
    return (time >= 360 && time <= 1080) ? M_TIME_DAYTIME : M_TIME_NIGHT;
}

void setupLights()
{
    // use pins: 10, 11, 12 and 13 for lights
    for(int i = 10; i < 14; ++i)
    {
        pinMode(i, OUTPUT);
    }
    for (int i = 10; i < 14; ++i)
    {
        digitalWrite(i, HIGH);
    }
    delay(500);
    for (int i = 10; i < 14; ++i)
    {
        digitalWrite(i, LOW);
    }
}

void setupSensors()
{
    // use pins: 6, 7, 8 and 9 for sensors
    for(int i = 6; i < 10; ++i)
    {
        pinMode(i, INPUT);
    }
    // setup something for sensors here!
}

void turnOnLight(ubyte pin)
{
    //Serial.println("*Turn on " + String((int)pin));
    // start at 10
    digitalWrite(pin, HIGH);
    setLightState(pin, 1);
}

void turnOffLight(ubyte pin)
{
    //Serial.println("*Turn off " + String((int)pin));
    // start at 10
    digitalWrite(pin, LOW);
    setLightState(pin, 0);
}

void applyConfigs()
{
    DataConfig* config = readDtConfig();
    ubyte n_lights = config->n_userConfigs;
    delete config;
    for(ubyte i = 0; i < n_lights; ++i)
    {
        applyConfig(i);
    }
}

void applyConfig(ubyte lightId)
{
    UserConfig* config;
    byte_t mode = readUserConfig(lightId, config);
    switch(mode)
    {
    case M_CONFIG_MODE_OFF:
        turnOffLight(m_highof(config->extra));
        break;
    case M_CONFIG_MODE_DEF:
        DefConfig* dconfig;
        readDefConfig(m_lowof(config->extra), dconfig);
        applyDefConfig(m_highof(config->extra),
                       m_lowof(config->sensor),
                       m_highof(config->sensor),
                       dconfig);
        delete dconfig;
        break;
    default:
        applyUsrConfig(m_highof(config->extra),
                       config);
        break;
    }
    delete config;
}

void applyDefConfig(ubyte pin,
                    ubyte lightSensor,
                    ubyte peopleSensor,
                    const DefConfig* config)
{
    bool isTime;
    ubyte offset = 0;
    ubyte n_configs = readDefConfig(&offset, config->state, &isTime);
    uint ret;
    ubyte timeType = getTimeType();

    for(ubyte i = 0; i < n_configs; ++i)
    {
        ret = readDefConfig(&offset, config->state, &isTime);

        if(isTime)
        {
            if(timeType == M_TIME_NIGHT)
            {
                if (getRelativelyTime(ret) <= getRelativelyTime(getCurrentTime()))
                {
                    turnOffLight(pin);
                    return;
                }
            }
        }
        else
        {
            switch(ret)
            {
            case M_LIGHTSENSOR_EVERYTIME:
                if(getLightSensor(lightSensor) <= config->lightThreshold)
                {
                    turnOnLight(pin);
                    return;
                }
                break;
            case M_LIGHTSENSOR_NIGHT:
                if((timeType == M_TIME_NIGHT) && (getLightSensor(lightSensor) <= config->lightThreshold))
                {
                    turnOnLight(pin);
                    return;
                }
                break;
            case M_PEOPLESENSOR_EVERYTIME:
                if(getPeopleSensor(peopleSensor))
                {
                    turnOnLight(pin);
                    return;
                }
                break;
            case M_PEOPLESENSOR_NIGHT:
                if((timeType == M_TIME_NIGHT) && getPeopleSensor(peopleSensor))
                {
                    turnOnLight(pin);
                    return;
                }
                break;
            case M_BOTHSENSOR_EVERYTIME:
                if((getLightSensor(lightSensor) <= config->lightThreshold) &&
                        (getPeopleSensor(peopleSensor)))
                {
                    turnOnLight(pin);
                    return;
                }
                break;
            case M_BOTHSENSOR_NIGHT:
                if((timeType == M_TIME_NIGHT) &&
                        (getLightSensor(lightSensor) <= config->lightThreshold) &&
                        (getPeopleSensor(peopleSensor)))
                {
                    turnOnLight(pin);
                    return;
                }
                break;
            }
        }
    }
    turnOffLight(pin);
}

void applyUsrConfig(ubyte pin,
                    const UserConfig* config)
{
    uint time = getCurrentTime();
    for(byte_t i = 0; i < config->n_configs; ++i)
    {
        if(isOnTime(time, config->lstTime[i]))
        {
            // simple, 0 is off and 1 is on
            if(config->lstConfig[i])
                turnOnLight(pin);
            else
                turnOffLight(pin);
            break;// ensure only config each light has active
        }
    }
}

ubyte getLightState(ubyte pin)
{
    uint state = __lights_state >> pin;
    return state & 1;
}

void setLightState(ubyte pin, ubyte state)
{
    uint mask = 1;
    uint vmask = state & 1;
    mask <<= pin;
    vmask <<= pin;
    __lights_state = (__lights_state & (~mask)) | vmask;
}
