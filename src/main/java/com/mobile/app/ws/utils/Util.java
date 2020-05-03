package com.mobile.app.ws.utils;

import java.util.Random;

public class Util {

    public static String generateRandomUserId() {
        Random rand = new Random();

        int rand_int1 = rand.nextInt(1000);
        int rand_int2 = rand.nextInt(1000);

        String userId = String.valueOf(String.valueOf(rand_int1) + String.valueOf(rand_int2));
        return userId;
    }
}
