#include "Lib.h"

ulong to_state(ubyte n, int l0, int l1, int l2, int l3)
{
    ulong state = n - 1;
    state <<= 30;// 2 high bits is n
    ubyte offset = 30;

    // saving level0
    if(l0 >= 0)
    {
        offset -= 5;
        state |= (ulong)l0 << offset;
    }
    else
    {
		if(l0 == -1440) l0 = 0;
        offset -= 12;
        state |= (-(ulong)l0 | 0x800) << offset;
    }
    if(n == 1) return state;

    // saving level1
    if(l1 >= 0)
    {
        offset -= 5;
        state |= (ulong)l1 << offset;
    }
    else
    {
		if(l1 == -1440) l1 = 0;
        offset -= 12;
        state |= (-(ulong)l1 | 0x800) << offset;
    }
    if(n == 2) return state;

    // saving level2
    if(l2 >= 0)
    {
        offset -= 5;
        state |= (ulong)l2 << offset;
    }
    else
    {
		if(l2 == -1440) l2 = 0;
        offset -= 12;
        state |= (-(ulong)l2 | 0x800) << offset;
    }
    if(n == 3) return state;

    // saving level3
    if(l3 >= 0)
    {
        offset -= 5;
        state |= (ulong)l3 << offset;
    }
    else
    {
		if(l3 == -1440) l3 = 0;
        offset -= 12;
        state |= (-(ulong)l3 | 0x800) << offset;
    }
    return state;
}

uint readDefConfig(ubyte* offset, ulong state, bool* isTime)
{
    if (*offset == 0)
    {
        *offset = 2;
        return (state >> 30) + 1;                  // return num of configs
    }
    else
    {
        *offset += 1;
        *isTime = (state >> (32 - *offset)) & 0x1;
        if (*isTime == 1)
        {
            *offset += 11;
            return (state >> (32 - *offset)) & 0x7FF;
        }
        else
        {
            *offset += 4;
            return (state >> (32 - *offset)) & 0x0F;
        }
    }
}

ubyte to_sensor(ubyte light_sensor_id, ubyte people_sensor_id)
{
    return m_toextra(people_sensor_id, light_sensor_id);
}

ubyte to_extra(ubyte light_type, ubyte light_pin)
{
    return m_toextra(light_pin, light_type);
}
