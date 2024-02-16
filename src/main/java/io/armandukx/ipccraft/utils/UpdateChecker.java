package io.armandukx.ipccraft.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.armandukx.ipccraft.IPCCraft;
import io.armandukx.ipccraft.handler.APIHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class UpdateChecker {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void check() {
        if (mc.world != null) {
            IPCCraft._STOPCHECKING = true;
            new Thread(() -> {
                System.out.println("Checking for updates...");
                JsonArray releases = APIHandler.getArrayResponse("https://api.modrinth.com/v2/project/vQSRr7O4/version");
                if (releases.size() > 0) {
                    for (int i = 0; i < releases.size(); i++) {
                        JsonObject release = releases.get(i).getAsJsonObject();
                        String versionNumber = release.get("version_number").getAsString().substring(1);
                        if (IPCCraft.MCVERSION.contains(versionNumber)) {
                            checkVersion(versionNumber);
                        }
                    }
                } else {
                    System.out.println("No releases found.");
                }
            }).start();
        }
    }

    public static void checkVersion(String versionNumber) {
        int[] IPCCraftParts = convertVersionStringToIntArray(IPCCraft.VERSION);
        int[] versionNumberParts = convertVersionStringToIntArray(versionNumber);
        int IPCCraftVersionInt = convertVersionPartsToInt(IPCCraftParts);
        int versionNumberInt = convertVersionPartsToInt(versionNumberParts);
        System.out.println(versionNumberInt + IPCCraftVersionInt);
        if (IPCCraftVersionInt < versionNumberInt) {
            String releaseURL = "https://modrinth.com/mod/vQSRr7O4/versions?g=" + IPCCraft.MCVERSION;
            LiteralText update = new LiteralText(Formatting.GREEN + "" + Formatting.BOLD + "  [UPDATE]  ");
            update.setStyle(update.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, releaseURL)));
            MutableText message = new LiteralText(Formatting.BOLD + IPCCraft.prefix + Formatting.DARK_RED + "IPCCraft " + IPCCraft.VERSION + " is outdated. Please update to " + versionNumber + ".\n").append(update);
            mc.player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
        }
    }

    public static int[] convertVersionStringToIntArray(String version) {
        String[] parts = version.split("\\.");
        int[] intArray = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            intArray[i] = Integer.parseInt(parts[i]);
        }
        return intArray;
    }

    public static int convertVersionPartsToInt(int[] parts) {
        int result = 0;
        for (int i = 0; i < parts.length; i++) {
            result += parts[i] * Math.pow(10, (parts.length - i - 1) * 2);
        }
        return result;
    }
}