#include "ds1307.h"

void initDS1307(int hour, int minute, int second)
{
    Wire.begin();
    setTime(hour, minute, second, 1, 3, 12, 2015);
}

unsigned int getTime()
{
    Wire.beginTransmission(DS1307);
    Wire.write((byte_t)0x00);
    Wire.endTransmission();
    Wire.requestFrom(DS1307, NumberOfFields);

    Wire.read(); // ignore second value
    int minute = bcd2dec(Wire.read());
    int hour   = bcd2dec(Wire.read() & 0x3F);

    return hour * 60 + minute;
}

int bcd2dec(byte_t num)
{
    return ((num/16 * 10) + (num % 16));
}

int dec2bcd(byte_t num)
{
    return ((num/10 * 16) + (num % 10));
}

void setTime(byte_t hr, byte_t min, byte_t sec, byte_t wd, byte_t d, byte_t mth, byte_t yr)
{
    Wire.beginTransmission(DS1307);
    Wire.write(byte_t(0x00)); // đặt lại pointer
    Wire.write(dec2bcd(sec));
    Wire.write(dec2bcd(min));
    Wire.write(dec2bcd(hr));
    Wire.write(dec2bcd(wd)); // day of week: Sunday = 1, Saturday = 7
    Wire.write(dec2bcd(d));
    Wire.write(dec2bcd(mth));
    Wire.write(dec2bcd(yr));
    Wire.endTransmission();
}
