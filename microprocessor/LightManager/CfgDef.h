#pragma once
#include "Config.h"
#include "Lib.h"

/**
*   the lights in livingroom will turn on when:
*   1: have people and the brightness is low
*   2: at night(time)
*   this lights will auto turn off after 22h00m if have not people
*/
const ulong G_LIGHT_LIVINGROOM_STATE =
    to_state(3, M_BOTHSENSOR_EVERYTIME, -1320, M_LIGHTSENSOR_NIGHT, 0);

/**
*   the lights in bedroom will turn on when:
*   1: have people, light is low and before 22h00m
*   this lights will auto turn off after 22h00m
*/
const ulong G_LIGHT_BEDROOM_STATE =
    to_state(2, M_BOTHSENSOR_EVERYTIME, -2760, 0, 0);

/**
*   the lights in kitchen will turn on when:
*   1: have people and the light is low
*   this lights do not auto turn off by time
*/
const ulong G_LIGHT_KITCHEN_STATE =
    to_state(1, M_BOTHSENSOR_EVERYTIME, 0, 0, 0);

/**
*   the lights in porch will turn on when:
*   1: only when the brightness is low
*   this lights do not auto turn off by time
*/
const ulong G_LIGHT_PORCH_STATE =
    to_state(1, M_LIGHTSENSOR_EVERYTIME, 0, 0, 0);

/**
*   the lights in bathroom will turn on when:
*   1: have people and brightness is low
*   this lights do not auto turn off by time
*/
const ulong G_LIGHT_BATHROOM_STATE =
    to_state(1, M_BOTHSENSOR_EVERYTIME, 0, 0, 0);

/**
*   the lights in toilet will turn on when:
*   1: have people and brightness is low
*   this lights do not auto turn off by time
*/
const ulong G_LIGHT_TOILET_STATE =
    to_state(1, M_BOTHSENSOR_EVERYTIME, 0, 0, 0);

/**
*   the lights in diningroom will turn on when:
*   1: have people and brightness is low
*   this lights do not auto turn off by time
*/
const ulong G_LIGHT_DININGROOM_STATE =
    to_state(1, M_BOTHSENSOR_EVERYTIME, 0, 0, 0);

/**
*   the lights in loftroom will turn on when:
*   1: have people and brightness is low
*   this lights do not auto turn off by time
*/
const ulong G_LIGHT_LOFTROOM_STATE =
    to_state(1, M_BOTHSENSOR_EVERYTIME, 0, 0, 0);
