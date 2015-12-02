#pragma once
#include "ConfigManager.h"
#define LIGHT_PIN_FROM 10

uint getCurrentTime();
uint getRelativelyTime(uint time);
bool isOnTime(uint intTime);
bool isOnTime(uint currentTime, uint intTime);
ubyte getTimeType();
void setupLights();
void setupSensors();
void turnOnLight(ubyte lightId);
void turnOffLight(ubyte lightId);
void applyConfigs();
void applyConfig(ubyte lightId);
void applyDefConfig(ubyte pin,
                    ubyte lightSensor,
                    ubyte peopleSensor,
                    const DefConfig* config);
void applyUsrConfig(ubyte pin,
                    const UserConfig* config);
ubyte getLightState(ubyte id);
void setLightState(ubyte id, ubyte state);// on/off
