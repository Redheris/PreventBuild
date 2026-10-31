package dev.redheris.preventbuild.util;

import net.minecraft.util.Mth;

public class ARGB {
    public static int black(float alpha) {
        return as8BitChannel(alpha) << 24;
    }

    public static int white(float alpha) {
        return as8BitChannel(alpha) << 24 | 0xFFFFFF;
    }

    public static int color(float alpha, int color) {
        return as8BitChannel(alpha) << 24 | color & 0xFFFFFF;
    }

    private static int as8BitChannel(float value) {
        return Mth.floor(value * 255.0F);
    }
}
