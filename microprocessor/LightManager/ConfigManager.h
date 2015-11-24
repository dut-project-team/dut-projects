#pragma once
#include "Lib.h"

/**
* initialize default configs and set user configs to zero
*/
void initConfigs();

/**
* clear all data from eeprom, reset all bytes to zero
*/
void clearAllConfigs();
/**
* reset all user configs to zero.
* nothing change, only count of configs will be set to zero
*/
void resetUserConfigs();
/**
* reset specified user config by id
* @param id a value between 0 and M_MAX_SUPPORT_USERCONFIGS - 1
*        unique value for each light
*/
void resetUserConfig(ubyte id);
/**
* reset specified default config by id
* @param lightType a unique value for each type of light
*/
void resetDefConfig(ubyte lightType);
/**
* reset all default configs to define from CfgDef.h
*/
void resetDefConfigs();
/**
* read user config from eeprom by id
* @param id a unique value for each light
* @param config a pointer to save out structure
*        of user config which was been read
* @return user config mode, 0 for user mode,
*         other defined from M_CONFIG_MODE_OFF and M_CONFIG_MODE_DEF
* @note you must delete config pointer when it's not used
*/
byte_t readUserConfig(ubyte id, UserConfig*& config);
/**
* read default config from eeprom by type of light
* @param lightType a unique value for each type of light
* @param config a pointer to save out structure of default
*        config which was been read
* @return always is 1
* @note you must delete config pointer when it's not used
*/
ubyte readDefConfig(ubyte lightType, DefConfig*& config);
/**
* write user config to eeprom by id
* @param id a unique value for each light
* @param config a pointer to user config structure
*         which will be read to write into eeprom
*/
void writeUserConfig(ubyte id, const UserConfig* config);
/**
* write default config to eeprom by type of light
* @param lightType a unique value for each type of light
* @param config a pointer to default config structure
*        which will be read to write into eeprom
*/
void writeDefConfig(ubyte lightType, const DefConfig* config);
/**
* read data config which store count of default config
* and count of user config from eeprom
* @return DataConfig structure which store count of def
*         config and count of user config
*/
DataConfig* readDtConfig();
/**
* retrieve default config structure which will save
* default values for each light type from CfgDef.h
* @param lightType a unique value for each type of light
* @return default config structure
* @note you must delete config which has been returned
*       when it's not used
*/
const DefConfig* getCfgDef(ubyte lightType);
/**
* add new user config to eeprom
* @param config a user config structure which
*        will be read to write into eeprom
* @return true if addition is successful
*/
bool addUserConfig(const UserConfig* config);
/**
* remove a exists user config from eeprom by id
* @param id a unique value for each user config
* @return true if remove is successful
*/
bool remUserConfig(ubyte id);
/**
* get offset of user configs from eeprom
* @note you don't need use this method
*/
int offset_of_usrcfgs();
/**
* get offset of default configs from eeprom
* @note you don't need use this method
*/
int offset_of_defcfgs();
/**
* get offset of data config from eeprom
*/
int offset_of_dtcfg();
