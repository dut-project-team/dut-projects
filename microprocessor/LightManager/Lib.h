#pragma once
#include <stdlib.h>
#include "Config.h"

#define m_safefree(ptr) {if(ptr != NULL) {delete ptr; ptr = NULL;}}
#define m_hasflag(state, flag) ((state & flag) == flag)
#define m_addflag(state, flag) {state |= flag;}
#define m_remflag(state, flag) {state &= ~flag;}
#define m_timeint(hour, minute) (hour * 60 + minute)
#define m_granttimeint(hour, minute) (hour * 60 + minute + M_TIMEOFF_RIORITY_BASE)
#define m_isgranttime(value) (value >= M_TIMEOFF_RIORITY_BASE)
#define m_lowof(extra) (extra & 0x0F)
#define m_highof(extra) (extra >> 4)
#define m_toextra(high, low) ((ubyte)((((ubyte)low) & 0x0F) | (((ubyte)high) << 4)))
#define m_pinof(extra) m_highof(extra)

typedef char byte_t;
typedef byte_t* pbyte;
typedef unsigned char ubyte;
typedef ubyte* pubyte;
typedef unsigned int uint;
typedef uint* puint;
typedef char* pchar;
typedef unsigned long ulong;
typedef ulong* pulong;

struct UserConfig
{
    // 4 low bits is light sensor, other is people sensor
    ubyte sensor;
	// 4 low bits is light type, 4 high bits is pin in adruino
	ubyte extra;
    // list of time and config
    uint lstTime[M_MAX_SUPPORT_USERCONFIGS];
    ubyte lstConfig[M_MAX_SUPPORT_USERCONFIGS];
    // count of actual config in the list
    // 0   have not any user config(user mode)
    // > 0 have n_configs user configs(user mode)
    // < 0 another mode, define by M_CONFIG_MODE_OFF or M_CONFIG_MODE_DEF
    byte_t n_configs = 0;
    char name[M_MAX_SUPPORT_USERNAME];
};

struct DefConfig
{
    // using percent
    ubyte lightThreshold = 10;
    // 2 high bit first: num of config
    // then, read 1 bit: 0 - config, 1 - time
    // config use 4 bit to save value(and 1 bit to indicate)
    // time use 11 bit to save value(and 1 bit to indicate)
	// note: with time, to define 0h you must set value to -1440
	// instead of 0. with other values must set to negative number
    ulong state;
};

struct DataConfig
{
    ubyte n_userConfigs = 0;
    ubyte n_defConfigs = 0;
    ubyte lstSensor[M_MAX_SUPPORT_SENSORS];
};

ulong to_state(ubyte n, int l0, int l1 = 0, int l2 = 0, int l3 = 0);
uint readDefConfig(ubyte* offset, ulong state, bool* isTime);
ubyte to_sensor(ubyte light_sensor_id, ubyte people_sensor_id);
ubyte to_extra(ubyte light_type, ubyte light_pin);
