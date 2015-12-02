#include <EEPROM.h>
#include <stddef.h>
#include "ConfigManager.h"
#include "CfgDef.h"
#include <Arduino.h>

void initConfigs()
{
    clearAllConfigs();
    DataConfig* dtConfig = new DataConfig();
    dtConfig->n_defConfigs = M_LIGHT_N_TYPES;
    dtConfig->n_userConfigs = 0;
    EEPROM.put(offset_of_dtcfg(), *dtConfig);
    delete dtConfig;
    resetDefConfigs();
}

void clearAllConfigs()
{
    int length = EEPROM.length();
    for (int i = 0; i < length; ++i)
    {
        EEPROM.write(i, 0);
    }
}

void resetUserConfigs()
{
    const DataConfig* dtConfig = readDtConfig();
    int usrAddress = offset_of_usrcfgs();
    int offsetOfNConfig = offsetof(struct UserConfig, n_configs);
    int usrSize = sizeof(UserConfig);
    for(int i = 0; i < dtConfig->n_userConfigs; ++i)
    {
        EEPROM.update(usrAddress + i * usrSize + offsetOfNConfig, 0);
    }
    delete dtConfig;
}

void resetDefConfigs()
{
    const DataConfig* dtConfig = readDtConfig();
    int defAddress = offset_of_defcfgs();
    int defSize = sizeof(DefConfig);
    for(int i = 0; i < dtConfig->n_defConfigs; ++i)
    {
        const DefConfig* defConfig = getCfgDef(i + M_LIGHT_LIVINGROOM);
        EEPROM.put(defAddress + i * defSize, *defConfig);
        delete defConfig;
    }
    delete dtConfig;
}

void resetUserConfig(ubyte id)
{
    int usrAddress = offset_of_usrcfgs();
    int offsetOfNConfig = offsetof(struct UserConfig, n_configs);
    int usrSize = sizeof(UserConfig);
    EEPROM.update(usrAddress + id * usrSize + offsetOfNConfig, 0);
}

void resetDefConfig(ubyte lightType)
{
    int defAddress = sizeof(DataConfig);
    int defSize = offset_of_defcfgs();
    const DefConfig* defConfig = getCfgDef(lightType);
    if(defConfig != NULL)
    {
        EEPROM.put(defAddress + (lightType - M_LIGHT_LIVINGROOM) * defSize, *defConfig);
        delete defConfig;
    }
}

byte_t readUserConfig(ubyte id, UserConfig*& config)
{
//    *config = new UserConfig();
//    EEPROM.get(offset_of_usrcfgs() + id * sizeof(UserConfig), *config);
//    return config->n_configs < 0 ? config->n_configs : 0;

    config = new UserConfig();

    config->sensor = m_toextra(7, 6);

    config->extra = m_toextra(12, M_LIGHT_LIVINGROOM);

    config->lstTime[0] = m_timeint(6,0);
    config->lstTime[0] = m_timeint(8,0);

    config->lstConfig[0] = 0;
    config->lstConfig[0] = 1;

    config->n_configs = M_CONFIG_MODE_DEF;

    return config->n_configs < 0 ? config->n_configs : 0;
}

ubyte readDefConfig(ubyte lightType, DefConfig*& config)
{
    config = new DefConfig();
    EEPROM.get(offset_of_defcfgs() + (lightType - M_LIGHT_LIVINGROOM) * sizeof(DefConfig), *config);
    return 1;
}

void writeUserConfig(ubyte id, const UserConfig* config)
{
    if(config == NULL)
        return;
    const DataConfig* dtConfig = readDtConfig();
    ubyte n_userConfigs = dtConfig->n_userConfigs;
    delete dtConfig;
    if(id >= n_userConfigs)
        return;
    EEPROM.put(offset_of_usrcfgs() + id * sizeof(UserConfig), *config);
}

void writeDefConfig(ubyte lightType, const DefConfig* config)
{
    if(config == NULL)
        return;
    const DataConfig* dtConfig = readDtConfig();
    ubyte n_defConfigs = dtConfig->n_defConfigs;
    delete dtConfig;
    ubyte id = lightType - M_LIGHT_LIVINGROOM;
    if(id >= n_defConfigs)
        return;
    EEPROM.put(offset_of_defcfgs() + id * sizeof(DefConfig), *config);
}

DataConfig* readDtConfig()
{
    DataConfig* config = new DataConfig();
    // data config begin the first block so address is zero
    EEPROM.get(0, *config);
    // ensure count of config greater or equal zero
    if(config->n_defConfigs < 0)
        config->n_defConfigs = 0;
    if(config->n_userConfigs < 0)
        config->n_userConfigs = 0;
    config->n_userConfigs = 1;
    return config;
}

DefConfig* getCfgDef(ubyte lightType)
{
    DefConfig* defConfig = new DefConfig();
    switch(lightType)
    {
    case M_LIGHT_LIVINGROOM:
        defConfig->state = G_LIGHT_LIVINGROOM_STATE;
        break;
    case M_LIGHT_BEDROOOM:
        defConfig->state = G_LIGHT_BEDROOM_STATE;
        break;
    case M_LIGHT_KITCHEN:
        defConfig->state = G_LIGHT_KITCHEN_STATE;
        break;
    case M_LIGHT_PORCH:
        defConfig->state = G_LIGHT_PORCH_STATE;
        break;
    case M_LIGHT_BATHROOM:
        defConfig->state = G_LIGHT_BATHROOM_STATE;
        break;
    case M_LIGHT_TOILET:
        defConfig->state = G_LIGHT_TOILET_STATE;
        break;
    case M_LIGHT_DININGROOM:
        defConfig->state = G_LIGHT_DININGROOM_STATE;
        break;
    case M_LIGHT_LOFTROOM:
        defConfig->state = G_LIGHT_LOFTROOM_STATE;
        break;
    default:
        delete defConfig;
        return NULL;
    }
    return defConfig;
}

bool addUserConfig(const UserConfig* config)
{
    if(config == NULL)
        return false;
    DataConfig* dtConfig = readDtConfig();
    if(dtConfig->n_userConfigs >= M_MAX_SUPPORT_USERS)
    {
        delete dtConfig;
        return false;
    }
    EEPROM.put(offset_of_usrcfgs() + (dtConfig->n_userConfigs - 1) * sizeof(UserConfig), *config);
    ++dtConfig->n_userConfigs;
    EEPROM.put(offset_of_dtcfg(), *dtConfig);
    delete dtConfig;
    return true;
}

bool remUserConfig(ubyte id)
{
    DataConfig* dtConfig = readDtConfig();
    if(id >= dtConfig->n_userConfigs)
    {
        delete dtConfig;
        return false;
    }
    int block = sizeof(UserConfig);
    int addr = offset_of_usrcfgs() + id * block;
    int i = addr;
    addr += (dtConfig->n_userConfigs - id - 2) * block;
    for(; i < addr; ++i)
    {
        EEPROM[i] = EEPROM[i + block];
    }
    --dtConfig->n_userConfigs;
    EEPROM.put(offset_of_dtcfg(), *dtConfig);
    delete dtConfig;
    return true;
}

int offset_of_usrcfgs()
{
    const DataConfig* dtConfig = readDtConfig();
    int usrAddress = offset_of_defcfgs() + sizeof(DefConfig) * dtConfig->n_defConfigs;
    delete dtConfig;
    return usrAddress;
}

int offset_of_defcfgs()
{
    return sizeof(DataConfig) + offset_of_dtcfg();
}

int offset_of_dtcfg()
{
    return 0;
}

