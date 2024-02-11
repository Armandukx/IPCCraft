package io.armandukx.rpccraft.config;

public class Configurations extends RPCConfig {

    @Entry
    public static boolean useClock = false;
    @Entry
    public static boolean sendConfigSettingsMessage = true;
    @Entry
    public static boolean useBrokenEnglish = false;
    @Entry
    public static boolean promoteRPCCraft = false;
    @Entry(dynamicTooltip = "button1UrlStrToolTip", max = 999)
    public static String button1Url = "null";
    @Entry(dynamicTooltip = "button1TextStrToolTip", max = 999)
    public static String button1Text = "null";
    @Entry(dynamicTooltip = "button2UrlStrToolTip", max = 999)
    public static String button2Url = "null";
    @Entry(dynamicTooltip = "button2TextStrToolTip", max = 999)
    public static String button2Text = "null";
    /*@Entry(min = 5)
    public static int intTest = 20;

    @Entry
    public static double decimalTest = 20;
*/
}