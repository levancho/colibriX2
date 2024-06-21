package com.despani.x2.core.utils;

import java.security.Principal;

public class AppUtils {


    public static int roleWeight(Principal p ) {

        int retvalue = 0;
        switch ("A") {

            case IRoles.ROLE_USER:
                retvalue=1;
                break;
            case IRoles.ROLE_EDITOR:
                retvalue=2;
                break;

            case IRoles.ROLE_MANAGER:
                retvalue=3;
                break;

            case IRoles.ROLE_ADMIN:
                retvalue=4;
                break;

            case IRoles.ROLE_SUPER_ADMIN:
                retvalue=5;
                break;

            default:
                break;


        }
        return retvalue;
    }
}
