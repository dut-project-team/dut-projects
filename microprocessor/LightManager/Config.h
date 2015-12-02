#pragma once

#define M_BAUD_RATE                         9600

/**
* config mode
*/
#define M_CONFIG_MODE_OFF                   -1 /* turn off the lights */
#define M_CONFIG_MODE_DEF                   -2 /* using default configs */

/**
* max support
*/
#define M_MAX_SUPPORT_USERS                 20 /* max number of the lights will be supported */
#define M_MAX_SUPPORT_USERCONFIGS           10 /* max number of configs for each light will be supported */
#define M_MAX_SUPPORT_USERNAME              12 /* max number of characters for each the light name */
#define M_MAX_SUPPORT_SENSORS               10 /* max number of sensors will be supported for each sensor type */

/**
* time
*/
#define M_TIME_DAYTIME                      0
#define M_TIME_NIGHT                        1
#define M_TIME_0H							1440

/**
* config using for def
*/
#define M_LIGHTSENSOR_EVERYTIME             0x01 /* use light sensor everytime */
#define M_LIGHTSENSOR_NIGHT                 0x02 /* only use light sensor at night*/
#define M_PEOPLESENSOR_EVERYTIME            0x03 /* use people sensor everytime */
#define M_PEOPLESENSOR_NIGHT                0x04 /* only use people sensor at night */
#define M_BOTHSENSOR_EVERYTIME              0x05 /* have people and light is low, using everytime*/
#define M_BOTHSENSOR_NIGHT                  0x06 /* have people and light is low, only night*/

/**
* light type
*/
#define M_LIGHT_N_TYPES                     8     /* count of the light types */
// phong khach
#define M_LIGHT_LIVINGROOM                  0x001 /* livingroom */
// phong ngu
#define M_LIGHT_BEDROOOM                    0x002 /* bedroom, sleep here */
// nha bep
#define M_LIGHT_KITCHEN                     0x003 /* kitchen, cook  here */
// truoc hien/cong
#define M_LIGHT_PORCH                       0x004 /* porch */
// phong tam
#define M_LIGHT_BATHROOM                    0x005 /* bathroom, batch here */
// nha ve sinh
#define M_LIGHT_TOILET                      0x006 /* toilet */
// phong an
#define M_LIGHT_DININGROOM                  0x007 /*dinningroom, eat here*/
// phong lam viec/hoc
#define M_LIGHT_LOFTROOM                    0x008 /* loftroom, work/study here */
