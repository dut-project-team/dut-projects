#ifndef DS1307
#define DS1307
#include <Wire.h>
#define DS1307 0x68
#define NumberOfFields 7

typedef char byte_t;

void initDS1307(int hour, int minute, int second);
unsigned int getTime();
int bcd2dec(byte_t num);
int dec2bcd(byte_t num);
void setTime(byte_t hr, byte_t min, byte_t sec, byte_t wd, byte_t d, byte_t mth, byte_t yr);

#endif
