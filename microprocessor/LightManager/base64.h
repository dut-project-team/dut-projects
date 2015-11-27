#ifndef BASE64_H
#define BASE64_H

#include <Arduino.h>
typedef char byte_t;

byte_t* base64Decode(String input, int* length);
String base64Encode(const byte_t* in, int length);

#endif
