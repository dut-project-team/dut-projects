#ifndef WSERVER_H_INCLUDED
#define WSERVER_H_INCLUDED
#include "base64.h"
#include "Package.h"

struct ByteHolder
{
    byte_t* buff;
    int length;
};

void setup_bluetooth();
void remote_control();
ByteHolder process_request(byte_t* decode, int length);
ByteHolder process_update_time(byte_t* decode, int length);
ByteHolder process_edit_defconfig(byte_t* data, int length);
ByteHolder process_edit_userconfig(byte_t* data, int length);
ByteHolder process_add_light(byte_t* data, int length);
ByteHolder process_remove_light(byte_t* data, int length);
ByteHolder process_get_userconfig(byte_t* data, int length);
ByteHolder process_get_defconfig(byte_t* data, int length);
ByteHolder process_get_lightstates(byte_t* data, int length);

#endif // WSERVER_H_INCLUDED
