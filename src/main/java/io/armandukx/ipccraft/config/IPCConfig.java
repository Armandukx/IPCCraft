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

public class IPCConfig {
    private static final Logger LOGGER = LogManager.getLogger(IPCClient.class);

    private final File configFile;

    public IPCConfig() {
        File IpccraftFolder = new File("config/ipccraft");
        if (!IpccraftFolder.exists()) {
            boolean folderCreated = IpccraftFolder.mkdirs();
            if (!folderCreated)
                LOGGER.debug("[IPCCraft] Failed to create ipccraft folder, settings will not be saved!");
        }
        this.configFile = new File("config/ipccraft/" + IPCCraft.MCVERSION + ".config");
        loadConfig();
    }

    private void loadConfig() {
        try {
            if (!this.configFile.exists()) {
                saveConfig();
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(this.configFile));
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    String key = tokens[0];
                    String value = tokens[1];
                    switch (key) {
                        case "useClock" -> CConfig.useClock = Boolean.parseBoolean(value);
                        case "sendConfigSettingsMessage" -> CConfig.sendConfigSettingsMessage = Boolean.parseBoolean(value);
                        case "useBrokenEnglish" -> CConfig.useBrokenEnglish = Boolean.parseBoolean(value);
                        case "promoteIPCCraft" -> CConfig.promoteIPCCraft = Boolean.parseBoolean(value);
                        case "button1Url" -> CConfig.button1Url = value;
                        case "button1Text" -> CConfig.button1Text = value;
                        case "button2Url" -> CConfig.button2Url = value;
                        case "button2Text" -> CConfig.button2Text = value;
                        case "useCustom" -> CConfig.LayoutConfig.useCustom = Boolean.parseBoolean(value);
                        case "customClientId" -> CConfig.LayoutConfig.clientId = Long.parseLong(value);
                        case "customDetailsString" -> CConfig.LayoutConfig.detailsString = value;
                        case "customStateString" -> CConfig.LayoutConfig.stateString = value;
                        case "customBigImageName" -> CConfig.LayoutConfig.bigImageName = value;
                        case "customBigImageText" -> CConfig.LayoutConfig.bigImageText = value;
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
            writer.write("useClock=" + CConfig.useClock + "\n");
            writer.write("sendConfigSettingsMessage=" + CConfig.sendConfigSettingsMessage + "\n");
            writer.write("useBrokenEnglish=" + CConfig.useBrokenEnglish + "\n");
            writer.write("promoteIPCCraft=" + CConfig.promoteIPCCraft + "\n");
            writer.write("button1Url=" + CConfig.button1Url + "\n");
            writer.write("button1Text=" + CConfig.button1Text + "\n");
            writer.write("button2Url=" + CConfig.button2Url + "\n");
            writer.write("button2Text=" + CConfig.button2Text + "\n");
            writer.write("useCustom=" + CConfig.LayoutConfig.useCustom + "\n");
            writer.write("-------CUSTOM PRESENCE-------\n");
            writer.write("customClientId=" + CConfig.LayoutConfig.clientId + "\n");
            writer.write("customDetailsString=" + CConfig.LayoutConfig.detailsString + "\n");
            writer.write("customStateString=" + CConfig.LayoutConfig.stateString + "\n");
            writer.write("customBigImageName=" + CConfig.LayoutConfig.bigImageName + "\n");
            writer.write("customBigImageText=" + CConfig.LayoutConfig.bigImageText + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}