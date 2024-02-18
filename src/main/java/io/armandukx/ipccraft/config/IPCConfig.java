package io.armandukx.ipccraft.config;

import io.armandukx.ipccraft.IPCCraft;
import io.armandukx.ipccraft.discordipc.IPCClient;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IPCConfig{
    private final File configFile;
    public IPCConfig() {
        File IpccraftFolder = new File("config/ipccraft");
        if (!IpccraftFolder.exists()) {
            boolean folderCreated = IpccraftFolder.mkdirs();
            Logger LOGGER = LogManager.getLogger(IPCClient.class);
            if (!folderCreated)
                LOGGER.debug("[IPCCraft] Failed to create ipccraft folder, settings will not be saved!");
        }
        this.configFile = new File("config/ipccraft/" + IPCCraft.MCVERSION + ".config");
        loadConfig();
    }

    private void loadConfig() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    String key = tokens[0];
                    String value = tokens[1];
                    switch (key) {
                        case "useClock" -> ClothConfig.useClock = Boolean.parseBoolean(value);
                        case "sendConfigSettingsMessage" -> ClothConfig.sendConfigSettingsMessage = Boolean.parseBoolean(value);
                        case "useBrokenEnglish" -> ClothConfig.useBrokenEnglish = Boolean.parseBoolean(value);
                        case "promoteIPCCraft" -> ClothConfig.promoteIPCCraft = Boolean.parseBoolean(value);
                        case "displayBiome" -> ClothConfig.displayBiome = Boolean.parseBoolean(value);
                        case "button1Url" -> ClothConfig.Buttons.button1Url = value;
                        case "button1Text" -> ClothConfig.Buttons.button1Text = value;
                        case "button2Url" -> ClothConfig.Buttons.button2Url = value;
                        case "button2Text" -> ClothConfig.Buttons.button2Text = value;

                        case "enabled" -> ClothConfig.Screenshots.enabled = Boolean.parseBoolean(value);
                        case "timer" -> ClothConfig.Screenshots.timer = Long.parseLong(value);

                        case "useCustom" -> ClothConfig.CustomPresence.useCustom = Boolean.parseBoolean(value);
                        case "customClientId" -> ClothConfig.CustomPresence.clientId = Long.parseLong(value);
                        case "customDetailsString" -> ClothConfig.CustomPresence.detailsString = value;
                        case "customStateString" -> ClothConfig.CustomPresence.stateString = value;
                        case "customBigImageName" -> ClothConfig.CustomPresence.bigImageName = value;
                        case "customBigImageText" -> ClothConfig.CustomPresence.bigImageText = value;
                        case "customSmallImageName" -> ClothConfig.CustomPresence.smallImageName = value;
                        case "customSmallImageText" -> ClothConfig.CustomPresence.smallImageText = value;
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.configFile));
            writer.write("useClock=" + ClothConfig.useClock + "\n");
            writer.write("sendConfigSettingsMessage=" + ClothConfig.sendConfigSettingsMessage + "\n");
            writer.write("useBrokenEnglish=" + ClothConfig.useBrokenEnglish + "\n");
            writer.write("promoteIPCCraft=" + ClothConfig.promoteIPCCraft + "\n");
            writer.write("displayBiome=" + ClothConfig.displayBiome + "\n");
            writer.write("button1Url=" + ClothConfig.Buttons.button1Url + "\n");
            writer.write("button1Text=" + ClothConfig.Buttons.button1Text + "\n");
            writer.write("button2Url=" + ClothConfig.Buttons.button2Url + "\n");
            writer.write("button2Text=" + ClothConfig.Buttons.button2Text + "\n");

            writer.write("enabled=" + ClothConfig.Screenshots.enabled + "\n");
            writer.write("timer=" + ClothConfig.Screenshots.timer + "\n");

            writer.write("useCustom=" + ClothConfig.CustomPresence.useCustom + "\n");
            writer.write("customClientId=" + ClothConfig.CustomPresence.clientId + "\n");
            writer.write("customDetailsString=" + ClothConfig.CustomPresence.detailsString + "\n");
            writer.write("customStateString=" + ClothConfig.CustomPresence.stateString + "\n");
            writer.write("customBigImageName=" + ClothConfig.CustomPresence.bigImageName + "\n");
            writer.write("customBigImageText=" + ClothConfig.CustomPresence.bigImageText + "\n");
            writer.write("customSmallImageName=" + ClothConfig.CustomPresence.smallImageName + "\n");
            writer.write("customSmallImageText=" + ClothConfig.CustomPresence.smallImageText + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}