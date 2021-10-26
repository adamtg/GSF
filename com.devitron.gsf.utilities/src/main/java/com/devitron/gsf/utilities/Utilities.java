package com.devitron.gsf.utilities;

import java.util.UUID;

public class Utilities {

    public static int compareVersions(String a, String b) {
        int r = 0;

        String[] ar = a.split(".");
        String[] br = b.split(".");

        int minLength = Math.min(ar.length, br.length);

        int ai = 0;
        int bi = 0;
        for (int i = 0; i < minLength; i++) {
            ai = Integer.valueOf(ar[i]);
            bi = Integer.valueOf(br[i]);

            if (ai > bi) {
                r = 1;
                break;
            } else if (ai < bi) {
                r = -1;
                break;
            }
        }

        if (r == 0) {

            if (ar.length < br.length) {
                r = 1;
            } else if (ar.length < br.length) {
                    r = -1;
                }
            }

        return r;
    }

    static public String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


    static public String generateRandomString(int size) {

        String wholeString = generateUUID();

        return wholeString.substring(0, size-1);

    }

}
