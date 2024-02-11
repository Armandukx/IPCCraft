package io.armandukx.rpccraft.utils;

import com.google.gson.JsonObject;
import io.armandukx.rpccraft.RPCCraft;
import io.armandukx.rpccraft.discordipc.IPCClient;
import io.armandukx.rpccraft.handler.APIHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateChecker {
    private static final Logger LOGGER = LogManager.getLogger(IPCClient.class);
    static boolean updateChecked = false;

    public static void init() {
        if (MinecraftClient.getInstance().world == null) return;
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!updateChecked) {
                updateChecked = true;

                new Thread(() -> {
                    MinecraftClient mc = MinecraftClient.getInstance();
                    LOGGER.debug("Checking for updates...");
                    JsonObject latestRelease = APIHandler.getResponse("https://api.modrinth.com/updates/vQSRr7O4/forge_updates.json", false);

                    LOGGER.debug("Has promos?");
                    if (latestRelease != null && latestRelease.has("promos")) {
                        JsonObject promos = latestRelease.getAsJsonObject("promos");
                        if (promos.has(RPCCraft.MCVERSION+"-recommended")) {
                            String recommendedVersion = promos.get(RPCCraft.MCVERSION+"-recommended").getAsString().substring(1);

                            String currentVersion = RPCCraft.VERSION;

                            if (currentVersion.compareTo(recommendedVersion) < 0) {
                                String releaseURL = "https://modrinth.com/mod/vQSRr7O4/versions?g="+RPCCraft.MCVERSION;

                                LiteralText update = new LiteralText(Formatting.GREEN + "" + Formatting.BOLD + "  [UPDATE]  ");
                                update.setStyle(update.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, releaseURL)));
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                if (mc.player != null){
                                    MutableText message = new LiteralText(Formatting.BOLD + RPCCraft.prefix + Formatting.DARK_RED + "RPCCraft " + RPCCraft.VERSION + " is outdated. Please update to " + recommendedVersion + ".\n").append(update);
                                    mc.player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(message)), false);
                                }
                            }
                        }
                    }
                }).start();
            }
        });
    }
}