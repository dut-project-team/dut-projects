#include "LibDemo.h"
#include <Arduino.h>
#include "Config.h"
#include "ConfigManager.h"
#include "CfgDef.h"
#include "Lib.h"

#define LED_MODE_OFF    10
#define LED_MODE_DEF    11
#define LED_MODE_USR    12

UserConfig* p_usrConf;
DefConfig*  p_defConf;
byte mode;
byte numOfLights;
ubyte i, j;

uint getSystemTimeDemo() {
    static unsigned char hour = 0;

    /* after exactly 1 second hour increase 1 unit */
    if (afterOneSecond()) {
        hour += 1;
        if (hour == 24) {
            hour = 0;
        }
    }

    return m_timeint(hour, 0);
}

void setupDemo() {
    for (i = 10; i <= 13; ++i) {
        m_setOutputPin(i);
    }
    numOfLights = readDtConfig()->n_userConfigs;
}

void loopDemo() {
    // turn on led demo after 12h
    if (getSystemTimeDemo() >= m_timeint(12, 0)) {
        m_setPin(LED_PIN_DEMO);
    } else {
        m_clrPin(LED_PIN_DEMO);
    }

    if (numOfLights != 0) {
        for (i = 0; i < numOfLights; i+= 1) {
            mode = readUserConfig(i, &p_usrConf);

            m_setOutputPin(m_pinof(p_usrConf->extra));
            switch (mode) {
            case M_CONFIG_MODE_OFF:
                // turn on led of mode off
                m_setPin(LED_MODE_OFF);
                break;
            case M_CONFIG_MODE_DEF:     // default mode
                // turn on led of default mode
                m_setPin(LED_MODE_DEF);
                readDefConfig(M_LIGHT_LIVINGROOM, &p_defConf);
                t_applyDefConfig(p_defConf->state, m_pinof(p_usrConf->extra));
                delete p_defConf;
                break;
            default:                    // user mode
                // turn on led of user mode
                m_setPin(LED_MODE_USR);

                // search mock time
                for (j = 0; j < p_usrConf->n_configs; j += 1) {
                    if (getSystemTimeDemo() >= p_usrConf->lstTime[j]) {
                        break;
                    }
                }

                // apply config suit
                if (p_usrConf->lstConfig[j] == 0) {
                    // turn off light
                    m_clrPin(m_pinof(p_usrConf->extra));
                } else {
                    // turn on light
                    m_setPin(m_pinof(p_usrConf->extra));
                }

                break;
            }

            delete p_usrConf;       // free memory
        }
    } else {

    }
}

bool afterOneSecond() {
    static unsigned long lastTime = 0;

    if (millis() - lastTime >= 1000) {
        lastTime = millis();
        return true;
    }

    return false;
}

void t_applyDefConfig(ulong state, byte_t pinLed) {
    ubyte offset = 0;
    bool isTime = false;
    uint config;
    byte_t numOfConf;

    numOfConf = readDefConfig(&offset, state, &isTime);
    for (byte_t i = 0; i < numOfConf; ++i) {
        config = readDefConfig(&offset, state, &isTime);
        if (isTime) {
            /* config at here is time to turn of light
                we just check with system time
                if ok we turn of light and break
                else read next config */
            if (getSystemTimeDemo() >= config) {
                /* turn of light */
                m_clrPin(pinLed);
                break;
            }
        } else {
            /* now, config containt config
                we just check that config to control light
                if ok we apply that config and break
                else read next config */
            if (true) {
                m_setPin(pinLed);
                break;
            }
        }
    }
}

















