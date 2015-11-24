#pragma once
/**
*   the values below can combine with others to save
*   more config in only state variant
*   NOTE: define have _TURNON_ can't combine with other _TURNON_
*/
#define M_LIGHT_TURNON_0    0x01 /* turn off the light */
#define M_LIGHT_TURNON_25   0x02 /* turn on the light with the brightness is 25% */
#define M_LIGHT_TURNON_50   0x04 /* turn on the light with the brightness is 50% */
#define M_LIGHT_TURNON_75   0x08 /* turn on the light with the brightness is 75% */
#define M_LIGHT_TURNON_100  0x10 /* turn on the light with the brightness is 100% */
#define M_LIGHT_HAVEPEOPLE  0x20 /* turn on the light when have people(using people sensor) */
#define M_LIGHT_LBRIGHTNESS 0x40 /* turn on the light when brightness is low(using light sensor) */
#define M_LIGHT_BOTHSENSOR  0x80 /* turn on the light when have people AND brightness is low */
