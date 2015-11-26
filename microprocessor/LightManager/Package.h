#ifndef PACKAGE_H
#define PACKAGE_H
#include "Lib.h"

#define COMMAND_EDIT_USERCONFIG			1
#define COMMAND_ADD_LIGHT				2
#define COMMAND_REMOVE_LIGHT			3
#define COMMAND_GET_USERCONFIG			4
#define COMMAND_GET_DEFCONFIG			5
#define COMMAND_GET_LIGHTSTATE			6
#define COMMAND_GET_AVAILABLE_CONFIG	7

class Package
{
protected:
    Package() {}
    virtual ~Package() {}
};

/// ************************************************************
/// REQUEST
/// ************************************************************

class RequestPackage: public Package
{
public:
    virtual bool init(const byte_t* st, int length) { return true; }
    RequestPackage() {}
};

// light_id + config
class RequestEditUCPackage: public RequestPackage
{
private:
    UserConfig* m_pconfig = NULL;
    byte_t m_id = -1;
public:
    UserConfig* get_config() { return m_pconfig; }
    byte_t get_id() { return m_id; }
    virtual bool init(const byte_t* st, int length);
    virtual ~RequestEditUCPackage() { m_safefree(m_pconfig) }
};

// config
class RequestAddLightPackage: public RequestPackage
{
private:
    UserConfig* m_pconfig = NULL;
public:
    UserConfig* get_config() { return m_pconfig; }
    virtual bool init(const byte_t* st, int length);
    virtual ~RequestAddLightPackage() { m_safefree(m_pconfig) }
};

// light_id
class RequestRemoveLightPackage: public RequestPackage
{
private:
    int m_id = -1;
public:
    byte_t get_id() { return m_id; }
    virtual bool init(const byte_t* st, int length);
};

/// ************************************************************
/// RESPONSE
/// ************************************************************
// success
class ResponsePackage: public Package
{
private:
    byte_t m_success;
public:
    byte_t is_success() { return m_success; }
    void set_success(byte_t success) { m_success = success; }
    virtual const byte_t* get_bytes(byte_t& length);
    ResponsePackage() {}
};

// success + count + configs
class ResponseUserConfigPackage: public ResponsePackage
{
private:
    UserConfig** m_ppconfig = NULL;
    byte_t m_count;
public:
    void set_configs(UserConfig** configs, byte_t count) { m_ppconfig = configs; m_count = count; }
    virtual const byte_t* get_bytes(byte_t& length);
};

// success + count + configs
class ResponseDefConfigPackage: public ResponsePackage
{
private:
    const DefConfig** m_ppconfig = NULL;
    byte_t m_count;
public:
    void set_configs(const DefConfig** configs, byte_t count) { m_ppconfig = configs; m_count = count; }
    virtual const byte_t* get_bytes(byte_t& length);
};

// success + count + states
class ResponseLightStatePackage: public ResponsePackage
{
private:
    byte_t m_count;
    const byte_t* m_pstates;
public:
    void set_states(const byte_t* states, byte_t count) { m_pstates = states; m_count = count; }
    virtual const byte_t* get_bytes(byte_t& length);
};

// success + pin_count + pins + light_sensor_count + light_sensors + people_sensor_count + people_sensors
class ResponseAvailableConfigsPackage: public ResponsePackage
{
private:
    byte_t m_pin_count;
    const byte_t* m_ppins;
    byte_t m_ls_count;
    const byte_t* m_pls;
    byte_t m_ps_count;
    const byte_t* m_pps;
public:
    void set_pins(const byte_t* pins, byte_t count) { m_ppins = pins; m_pin_count = count; }
    void set_ls(const byte_t* ls, byte_t count) { m_pls = ls; m_ls_count = count; }
    void set_ps(const byte_t* ps, byte_t count) { m_pps = ps; m_ps_count = count; }
    virtual const byte_t* get_bytes(byte_t& length);
};

class ResponseAddLightPackage: public ResponsePackage
{
private:
    byte_t m_new_id;
public:
    void set_new_id(byte_t new_id) { m_new_id = new_id; }
    virtual const byte_t* get_bytes(byte_t& length);
};
#endif // PACKAGE_H
