#pragma once
#include "ConfigManager.h"

uint getCurrentTime();
uint getRelativelyTime(uint time);
bool isOnTime(uint intTime);
bool isOnTime(uint currentTime, uint intTime);
ubyte getTimeType();
void setupLights();
void setupSensors();
void turnOnLight(ubyte pin);
void turnOffLight(ubyte pin);
void applyConfigs();
void applyConfig(ubyte lightId);
void applyDefConfig(ubyte pin,
                    ubyte lightSensor,
                    ubyte peopleSensor,
                    const DefConfig* config);
void applyUsrConfig(ubyte pin,
                    const UserConfig* config);
ubyte getLightState(ubyte pin);
void setLightState(ubyte pin, ubyte state);// on/off
