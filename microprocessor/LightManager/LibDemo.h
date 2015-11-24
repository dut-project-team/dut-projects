#ifndef LIBDEMO_H
#define LIBDEMO_H

#include "Lib.h"

#define LED_MODE_OFF    10
#define LED_MODE_DEF    11
#define LED_MODE_USR    12
#define LED_PIN_DEMO    13
#define DELAY_TIME      1000    // 1000 ms = 1 s
#define m_setOutputPin(pin) pinMode((pin), OUTPUT)
#define m_setPin(pin) digitalWrite((pin), HIGH)
#define m_clrPin(pin) digitalWrite((pin), LOW)

uint getSystemTimeDemo();
void setupDemo();
void loopDemo();
bool afterOneSecond();
void t_applyDefConfig(ulong state, byte_t pinLed);

#endif // LIBDEMO_H
