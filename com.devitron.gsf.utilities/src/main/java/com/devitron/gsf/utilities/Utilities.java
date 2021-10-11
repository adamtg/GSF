package com.devitron.gsf.utilities;

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

}
