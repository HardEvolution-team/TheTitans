package com.electrifiedded.thetitans.util;

import net.minecraft.util.text.translation.I18n;

public class Utils {
    public static String translateMult(String key, int amount, Object... values) {
        if (amount > 1) {
            int index = Maths.random(1, amount);
            if (index > 1)
                translate(key + "." + index, values);
        }

        return translate(key, values);
    }

    @SuppressWarnings("deprecation")
    public static String translate(String key, Object... values) {
        String value = I18n.translateToLocalFormatted(key, values);
        return value;
    }
}
