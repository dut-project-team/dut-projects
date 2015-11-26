#include "Package.h"
#include <string.h>

///-------------------------------------------------------------
bool RequestEditUCPackage::init(const byte_t* st, int length)
{
    int sz_uconfig = sizeof(UserConfig);
    // skip 1 byte for command
    // skip 1 byte for light id(next byte)
    if(sz_uconfig + 2 != length)
        return false;

    m_id = *(st + 1);
    m_pconfig = new UserConfig();
    memcpy(m_pconfig, st + 2, sz_uconfig);

    return true;
}
///-------------------------------------------------------------
bool RequestAddLightPackage::init(const byte_t* st, int length)
{
    int sz_uconfig = sizeof(UserConfig);
    // skip 1 byte for command
    if(sz_uconfig + 1 != length)
        return false;

    m_pconfig = new UserConfig();
    memcpy(m_pconfig, st + 1, sz_uconfig);

    return true;
}
///-------------------------------------------------------------
bool RequestRemoveLightPackage::init(const byte_t* st, int length)
{
    if(length != 2)
        return false;
    // skip 1 byte for command
    m_id = *(st + 1);
    return true;
}
///-------------------------------------------------------------
const byte_t* ResponsePackage::get_bytes(uint& length)
{
    byte_t* st = new byte_t[1];
    st[0] = m_success;
    length = 1;
    return st;
}
///-------------------------------------------------------------
const byte_t* ResponseUserConfigPackage::get_bytes(uint& length)
{
    const byte_t* header = ResponsePackage::get_bytes(length);
    int sz_uconfig = sizeof(UserConfig);
    int sz_extra = sz_uconfig * m_count;
    // header + count + configs
    byte_t* bytes = new byte_t[length + 1 + sz_extra];
    memcpy(bytes, header, length);
    delete[] header;
    *(bytes + length) = m_count;
    for(byte_t i = 0; i < m_count; i++)
    {
        memcpy(bytes + length + 1 + sz_uconfig * i, *(m_ppconfig + i), sz_uconfig);
    }
    length += sz_extra + 1;
    return bytes;
}
///-------------------------------------------------------------
const byte_t* ResponseDefConfigPackage::get_bytes(uint& length)
{
    const byte_t* header = ResponsePackage::get_bytes(length);
    int sz_dconfig = sizeof(DefConfig);
    int sz_extra = sz_dconfig * m_count;
    // header + count + configs
    byte_t* bytes = new byte_t[length + 1 + sz_extra];
    memcpy(bytes, header, length);
    delete[] header;
    *(bytes + length) = m_count;
    for(byte_t i = 0; i < m_count; i++)
    {
        memcpy(bytes + length + 1 + sz_dconfig * i, *(m_ppconfig + i), sz_dconfig);
    }
    length += sz_extra + 1;
    return bytes;
}
///-------------------------------------------------------------
const byte_t* ResponseLightStatePackage::get_bytes(uint& length)
{
    const byte_t* header = ResponsePackage::get_bytes(length);
    byte_t* bytes = new byte_t[length + 1 + m_count];
    memcpy(bytes, header, length);
    delete[] header;
    *(bytes + length) = m_count;
    memcpy(bytes + length + 1, m_pstates, m_count);
    length += m_count + 1;
    return bytes;
}
///-------------------------------------------------------------
const byte_t* ResponseAvailableConfigsPackage::get_bytes(uint& length)
{
    const byte_t* header = ResponsePackage::get_bytes(length);
    byte_t sz_total = length + 3 + m_pin_count + m_ls_count + m_ps_count;
    byte_t* bytes = new byte_t[sz_total];
    // header
    memcpy(bytes, header, length);
    delete[] header;

    int offset = length;
    // pins
    *(bytes + offset) = m_pin_count;
    offset += 1;
    memcpy(bytes + offset, m_ppins, m_pin_count);
    offset += m_pin_count;
    // light sensors
    *(bytes + offset) = m_ls_count;
    offset += 1;
    memcpy(bytes + offset, m_pls, m_ls_count);
    offset += m_ls_count;
    // people sensors
    *(bytes + offset) = m_ps_count;
    offset += 1;
    memcpy(bytes + offset, m_pps, m_ps_count);

    length = sz_total;
    return bytes;
}
///-------------------------------------------------------------
const byte_t* ResponseAddLightPackage::get_bytes(uint& length)
{
    const byte_t* header = ResponsePackage::get_bytes(length);
    byte_t* bytes = new byte_t[length + 1];
    memcpy(bytes, header, length);
    delete[] header;
    *(bytes + length) = m_new_id;
    length += 1;
    return bytes;
}
