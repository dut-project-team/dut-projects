#include <EEPROM.h>/* global variable */
#include "Config.h"
#include "ConfigManager.h"
#include "CfgDef.h"

uint systemtime;

#define LED_MODE_OFF    10
#define LED_MODE_DEF    11
#define LED_MODE_USR    12

void setup(){
    systemtime = 0;
    for (int i = 10; i <= 12; i += 1) {
        pinMode(i, OUTPUT);
        digitalWrite(i, LOW);
    }
}

void loop(){
    UserConfig* puserConf;
    byte mode;
    byte numOfLights = readDtConfig()->n_userConfigs;

    static uint hour = 5;
    delay(1000);
    hour += 1;
    if (hour >= 9) hour = 5;
    systemtime = m_timeint(hour,0);

    for (int i = 0; i < numOfLights; i += 1) {
        mode = readUserConfig(i, &puserConf);

        pinMode(m_pinof(puserConf->extra), OUTPUT);
        switch (mode) {
        case M_CONFIG_MODE_OFF:
            //debug
            digitalWrite(LED_MODE_OFF, HIGH);
            // tat den
            digitalWrite(m_pinof(puserConf->extra), LOW);
            break;
        case M_CONFIG_MODE_DEF:
            //debug
            digitalWrite(LED_MODE_DEF, HIGH);
            // bat den
            digitalWrite(m_pinof(puserConf->extra), HIGH);
            // doc def config
            // ap dung
            break;
        default:
            //debug
            digitalWrite(LED_MODE_USR, HIGH);
            // xem thoi gian he thong

            // so sanh vs list time
            int t;
            for (t = 0; t < puserConf->n_configs; t += 1) {
                if (systemtime >= puserConf->lstTime[t]) {
                    break;
                }
            }

            // get config tuong ung
            if((puserConf->lstConfig[t]) == 0) {
                // tat den
                digitalWrite(m_pinof(puserConf->extra), LOW);
            } else {
                // bat den
                digitalWrite(m_pinof(puserConf->extra), HIGH);
            }

            break;
        }

        delete puserConf;
    }
}

