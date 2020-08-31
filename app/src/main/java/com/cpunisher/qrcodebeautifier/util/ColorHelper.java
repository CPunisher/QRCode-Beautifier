package com.cpunisher.qrcodebeautifier.util;

public class ColorHelper {

    public static String toColorHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
