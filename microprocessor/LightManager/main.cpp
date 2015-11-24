#include "Package.h"
#include <stdio.h>
#include <string.h>

int main()
{
    UserConfig u;
    u.sensor = 1;
    u.extra = 2;
    u.n_configs = 3;
    int sz = sizeof(UserConfig);
    char* st = new char[sz + 1];
    memcpy(st, &u, sizeof(UserConfig));

    printf("%s\n", st);
    RequestEditUCPackage* pk = new RequestEditUCPackage();
    //printf("%d\n", pk->init(st));
}
