package io.armandukx.ipccraft.utils;

public class TimeConverter {
    public static String convert(long minecraftTime) {
        return String.format("%02d:%02d", minecraftTime * 6 / 100 / 60, minecraftTime * 6 / 100 % 60);
    }
}
