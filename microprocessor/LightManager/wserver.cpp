#include "wserver.h"
#include "ConfigManager.h"
#include "Monitor.h"
#include <SoftwareSerial.h>

SoftwareSerial BTSerial(2, 3);

void log(String st)
{
    Serial.println(st);
    Serial.flush();
}

// overwrite data to eeprom from def_config_od
ByteHolder process_edit_defconfig(byte_t* data, int length)
{
    // 1(request_type) + 1(defconfig_id) + remain(defconfig_data)
    ubyte defconfig_id = data[1];
    byte_t* defconfig_data = data + 2;
    // update to eeprom

    // response is true(always)
    byte_t* response = new byte_t[1];
    response[0] = 1;// always true
    ByteHolder holder;
    holder.buff = response;
    holder.length = 1;
    return holder;
}

// overwrite data to eeprom from light_id
ByteHolder process_edit_userconfig(byte_t* data, int length)
{
    // 1(request_type) + 1(light_id) + remain(user_config_data)
    ubyte light_id = data[1];
    byte_t* config_data = data + 2;
    // update to eeprom

    // response is true(always)
    byte_t* response = new byte_t[1];
    response[0] = 1;// always true
    ByteHolder holder;
    holder.buff = response;
    holder.length = 1;
    return holder;
}

// check if eeprom has max lights then response false
// else add new userconfig then response true
ByteHolder process_add_light(byte_t* data, int length)
{
    byte_t* response = new byte_t[1];
    response[0] = 0;

    // check if available slot
    if (true)// wait for me implement it
    {
        // 1(request_type) + remain(new_user_config_data)
        byte_t* config_data = data + 1;
        // add to eeprom

        // update dataConfig

        // set response value is true
        response[0] = 1;

        log("add new light");
    }

    ByteHolder holder;
    holder.buff = response;
    holder.length = 1;
    return holder;
}

// check light_id is alive then remove from eeprom
ByteHolder process_remove_light(byte_t* data, int length)
{
    byte_t* response = new byte_t[1];
    response[0] = 0;

    // check if light_id is alive
    if (true)// wait for me implement it
    {
        // 1(request_type) + 1(remove_light_id)
        ubyte light_id = data[1];
        // remove from eeprom

        // update dataConfig

        // set response value is true
        response[0] = 1;
        log("remove light id " + String((int)light_id));
    }

    ByteHolder holder;
    holder.buff = response;
    holder.length = 1;
    return holder;
}

ByteHolder process_get_userconfig()
{
    // get count of light
    DataConfig* dtConfig = readDtConfig();
    ubyte count = dtConfig->n_userConfigs;
    delete dtConfig;

    UserConfig** userConfigs = new UserConfig*[count];
    for (ubyte i = 0; i < count; ++i)
    {
        UserConfig* userConfig;
        readUserConfig(i, userConfig);
        userConfigs[i] = userConfig;
    }

    ResponseUserConfigPackage* obj = new ResponseUserConfigPackage();
    obj->set_configs(userConfigs, count);
    uint length;
    byte_t* bytes = obj->get_bytes(length);
    delete obj;
    for (ubyte i = 0; i < count; ++i)
    {
        delete userConfigs[i];
    }
    delete[] userConfigs;

    ByteHolder holder;
    holder.buff = bytes;
    holder.length = length;
    return holder;
}

ByteHolder process_get_defconfig()
{
    DefConfig* defConfigs[M_LIGHT_N_TYPES];
    for (ubyte i = 0; i < M_LIGHT_N_TYPES; ++i)
    {
        DefConfig* obj;
        readDefConfig(M_LIGHT_LIVINGROOM + i, obj);
        defConfigs[i] = obj;
    }

    ResponseDefConfigPackage* obj = new ResponseDefConfigPackage();
    obj->set_configs(defConfigs, M_LIGHT_N_TYPES);

    // convert
    uint length;
    byte_t* bytes = obj->get_bytes(length);

    delete obj;
    for (ubyte i = 0; i < M_LIGHT_N_TYPES; ++i)
    {
        delete defConfigs[i];
    }

    ByteHolder holder;
    holder.buff = bytes;
    holder.length = length;

    return holder;
}

ByteHolder process_get_lightstates()
{
    // get count of light
    DataConfig* dtConfig = readDtConfig();
    ubyte count = dtConfig->n_userConfigs;
    delete dtConfig;

    log("get lights count = " + String((int)count));

    // get lights state
    byte_t* states = new byte_t[count];
    for (ubyte i = 0; i < count; ++i)
    {
        UserConfig* userConfig;
        readUserConfig(i, userConfig);
        ubyte lightId = m_highof(userConfig->extra);
        states[i] = getLightState(lightId);
        delete userConfig;
    }

    // convert to bytes array
    ResponseLightStatePackage* obj = new ResponseLightStatePackage();
    obj->set_states(states, count);
    uint length;
    byte_t* bytes = obj->get_bytes(length);
    delete[] states;
    delete obj;

    ByteHolder holder;
    holder.buff = bytes;
    holder.length = length;

    return holder;
}

ByteHolder process_request(byte_t* decode, int length)
{
    switch (decode[0])
    {
    case COMMAND_EDIT_USERCONFIG:
        return process_edit_userconfig(decode, length);
    case COMMAND_ADD_LIGHT:
        return process_add_light(decode, length);
    case COMMAND_REMOVE_LIGHT:
        return process_remove_light(decode, length);
    case COMMAND_GET_USERCONFIG:
        return process_get_userconfig();
    case COMMAND_GET_DEFCONFIG:
        return process_get_defconfig();
    case COMMAND_GET_LIGHTSTATE:
        return process_get_lightstates();
    }
    ByteHolder holder;
    holder.buff = NULL;
    holder.length = 0;
    return holder;
}

void setup_bluetooth()
{
    BTSerial.begin(9600);
}

void remote_control()
{
    if (BTSerial.available() <= 0) return;
    log("................................");
    log("Requesting...");
    char c_ctrl = BTSerial.read();
    byte_t* req = NULL;
    int req_len = 0;
    switch (c_ctrl)
    {
    case COMMAND_GET_USERCONFIG:
    case COMMAND_GET_DEFCONFIG:
    case COMMAND_GET_LIGHTSTATE:
        req_len = 1;
        req = new byte_t[req_len];
        break;
    case COMMAND_ADD_LIGHT:
    case COMMAND_REMOVE_LIGHT:
        req_len = 2;
        req = new byte_t[req_len];
        req[1] = BTSerial.read();// get index of light
        break;
    case COMMAND_EDIT_USERCONFIG:
        // request_type + light_id + userconfig_data
        req_len = 1 + 1 + sizeof(UserConfig);
        req = new byte_t[req_len];
        for (ubyte i = 1; i < req_len; ++i)
        {
            // read actual data include light_id(first byte) and new userconfig_data(remain)
            req[i] = BTSerial.read();
        }
        break;
    case COMMAND_EDIT_DEFCONFIG:
        // request_type + defconfig_id + defconfig_data
        req_len = 1 + 1 + sizeof(DefConfig);
        req = new byte_t[req_len];
        for (ubyte i = 1; i < req_len; ++i)
        {
            // read actual data include light_id(first byte) and new defconfig_data(remain)
            req[i] = BTSerial.read();
        }
        break;
    }
    if (req_len)
    {
        // save request type to request_data
        req[0] = c_ctrl;

        log("Process request...");
        ByteHolder response = process_request(req, req_len);
        delete[] req;
        if (response.length > 0)
        {
            log("Sending data...");
            // send data length(1 byte, max is 255 elements)
            BTSerial.write((ubyte)response.length);
            log("Sent length is " + String(response.length));
            // send actual data with length had sent
            for (int i = 0; i < response.length; ++i)
            {
                BTSerial.write(response.buff[i]);
                BTSerial.flush();
            }
            log("Done!");
            delete[] response.buff;
        }
    }
    else
    {
        log("Wrong request!");
    }
    log("Clear buffer!");
    // clear buffer
    while (BTSerial.available())
    {
        BTSerial.read();
    }
    log("Wait for new request...");
}
