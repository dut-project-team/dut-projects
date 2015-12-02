#include "base64.h"
#include <Arduino.h>

typedef char byte_t;

String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.";
extern void log(String, String);
byte_t* base64Decode(String input, int* out_length)
{
    int length = input.length();
    if (length % 4 != 0)
        return NULL;
    *out_length = ((length * 3) / 4)
                  - (input.indexOf('.') > 0 ? (length - input.indexOf('.')) : 0);
    byte_t* decoded = new byte_t[*out_length];
    const char* inChars = input.c_str();
    int j = 0;
    int* b = new int[4];
    for (int i = 0; i < length; i += 4)
    {
        // This could be made faster (but more complicated) by precomputing
        // these index locations
        b[0] = CODES.indexOf(inChars[i]);
        b[1] = CODES.indexOf(inChars[i + 1]);
        b[2] = CODES.indexOf(inChars[i + 2]);
        b[3] = CODES.indexOf(inChars[i + 3]);
        decoded[j++] = (byte_t) ((b[0] << 2) | (b[1] >> 4));
        if (b[2] < 64)
        {
            decoded[j++] = (byte_t) ((b[1] << 4) | (b[2] >> 2));
            if (b[3] < 64)
            {
                decoded[j++] = (byte_t) ((b[2] << 6) | b[3]);
            }
        }
    }
    delete[] b;
    return decoded;
}

String base64Encode(const byte_t* in, int length)
{
    int buff_size = (length * 4) / 3 + 4;
//    char* buff = new char[buff_size];
//    buff[buff_size - 1] = '\0';
//    buff[buff_size - 2] = '\0';
//    buff[buff_size - 3] = '\0';
//    buff[buff_size - 4] = '\0';
    String ret = "";
    int b;
    for (int i = 0, j = 0; i < length; i += 3)
    {
        b = (in[i] & 0xFC) >> 2;
        //buff[j++] = CODES[b];
        ret += CODES[b];
        b = (in[i] & 0x03) << 4;
        if (i + 1 < length)
        {
            b |= (in[i + 1] & 0xF0) >> 4;
            //buff[j++] = CODES[b];
            ret += CODES[b];
            b = (in[i + 1] & 0x0F) << 2;
            if (i + 2 < length)
            {
                b |= (in[i + 2] & 0xC0) >> 6;
                //buff[j++] = CODES[b];
                ret += CODES[b];
                b = in[i + 2] & 0x3F;
                //buff[j++] = CODES[b];
                ret += CODES[b];
            }
            else
            {
                //buff[j++] = CODES[b];
                ret += CODES[b];
                //buff[j++] = '.';
                ret += '.';
            }
        }
        else
        {
//            buff[j++] = CODES[b];
//            buff[j++] = '.';
//            buff[j++] = '.';
              ret += CODES[b];
              ret += "..";
        }
    }
    //String ret(buff);
    //delete[] buff;
    return ret;
}
