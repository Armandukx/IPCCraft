package io.armandukx.ipccraft.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.PrefixText;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;

@Config(name = "ipccraft")
public class ClothConfig implements ConfigData {
    public static boolean useClock = false;
    public static boolean sendConfigSettingsMessage = true;
    public static boolean useBrokenEnglish = false;
    public static boolean promoteIPCCraft = false;
    public static String button1Url = "";
    public static String button1Text = "";
    public static String button2Url = "";
    public static String button2Text = "";
    @Category("custom")
    @TransitiveObject
    public static CustomPresence customPresence = new CustomPresence();
    public static class CustomPresence {
        @PrefixText @Tooltip public static boolean useCustom = false;
        @Tooltip public static long clientId;
        public static String detailsString;
        public static String stateString;
        public static String bigImageName;
        public static String bigImageText;
        public static String smallImageName;
        public static String smallImageText;
    }
}
