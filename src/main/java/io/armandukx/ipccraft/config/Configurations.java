package io.armandukx.ipccraft.config;

public class Configurations extends IPCConfig {
    @Entry
    public static boolean useClock = false;
    @Entry
    public static boolean sendConfigSettingsMessage = true;
    @Entry
    public static boolean useBrokenEnglish = false;
    @Entry
    public static boolean promoteIPCCraft = false;
    @Entry(dynamicTooltip = "button1UrlStrToolTip", max = 999)
    public static String button1Url = "null";
    @Entry(dynamicTooltip = "button1TextStrToolTip", max = 999)
    public static String button1Text = "null";
    @Entry(dynamicTooltip = "button2UrlStrToolTip", max = 999)
    public static String button2Url = "null";
    @Entry(dynamicTooltip = "button2TextStrToolTip", max = 999)
    public static String button2Text = "null";
    @Entry
    public static boolean useCustom = false;
}