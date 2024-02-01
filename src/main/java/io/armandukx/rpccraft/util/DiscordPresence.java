package io.armandukx.rpccraft.util;

import io.armandukx.rpccraft.RPCCraft;
import io.armandukx.rpccraft.config.Configurations;
import io.armandukx.rpccraft.discordipc.IPCClient;
import io.armandukx.rpccraft.discordipc.IPCListener;
import io.armandukx.rpccraft.discordipc.entities.RichPresence;
import io.armandukx.rpccraft.discordipc.entities.pipe.PipeStatus;
import io.armandukx.rpccraft.discordipc.exceptions.NoDiscordClientException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.time.OffsetDateTime;

public class DiscordPresence {
    IPCClient client = new IPCClient(1200554143874555935L);
    RichPresence.Builder builder = new RichPresence.Builder();
    MinecraftClient instance = MinecraftClient.getInstance();
    Screen currentScreen = instance.currentScreen;
    private boolean sentMultiConfigMessage = false;
    public DiscordPresence(){
        try {
            client.setListener(new Listener());
            client.connect();
            client.sendRichPresence(builder.setStartTimestamp(OffsetDateTime.now()).build());
        } catch (NoDiscordClientException e) {
            System.out.println("Discord not found.");
        }
    }

    public void Update(String details, String state, String largeImageText, String largeImageKey, String smallImageKey, String smallImageText){
        if (client != null && client.getStatus() == PipeStatus.CONNECTED) {
            client.sendRichPresence(builder.setDetails(details)
                    .setDetails(details)
                    .setState(state)
                    .setLargeImage(largeImageKey, largeImageText)
                    .setSmallImage(smallImageKey,smallImageText).build());
        }
    }

    public void clearPresence()
    {
        if (client != null && client.getStatus() == PipeStatus.CONNECTED) {
            client.sendRichPresence(null);
        }
    }

    public String[] getWorldInfo(World world, String imageText) {
        if (CheckWorld.isSinglePlayer(world)) {
            String DetailsString = (Configurations.useBrokenEnglish ? "Currntli" : "Currently") + " in The " + imageText;
            String StateString = null;
            if (instance.player != null) {
                StateString = "Playing Singleplayer | In the " + world.getBiome(instance.player.getBlockPos()).getCategory().name().toLowerCase() + " biome";
            }
            return new String[]{DetailsString, StateString};
        }
            else
            {
                ClientPlayNetworkHandler networkHandler = instance.getNetworkHandler();
                if (networkHandler != null) {
                    String ServerAddress = networkHandler.getConnection().getAddress().toString().split("[/\\\\]")[0];
                    int playerCount = networkHandler.getPlayerList().size();

                    String DetailsString = Configurations.useBrokenEnglish ? "Playin wit " : "Playing with ";
                    DetailsString += playerCount + (Configurations.useBrokenEnglish ? " Minecrafters" : " Players");
                    String StateString = "Playing Multiplayer";

                    imageText = ServerAddress;
                    String imageKey = "https://eu.mc-api.net/v3/server/favicon/" + ServerAddress;
                    if (Configurations.sendConfigSettingsMessage && !sentMultiConfigMessage) {
                        MinecraftClient.getInstance().player.sendMessage(Text.Serializer.fromJson(Text.Serializer.toJson(new LiteralText(RPCCraft.prefix + Formatting.RED + "To configure settings you must be in a singleplayer world!"))), false);
                        sentMultiConfigMessage = true;
                    }

                    return new String[]{DetailsString, StateString, imageText, imageKey};
                }
            }
            return null;
    }

    public String[] getSmallImageInfo(World world) {
        String smallImageKey;
        String smallImageText;

        if (Configurations.useClock && world != null) {
            smallImageKey = "https://static.wikia.nocookie.net/minecraft_gamepedia/images/3/3e/Clock_JE3_BE3.gif/revision/latest?cb=20201125194053";
            smallImageText = "World time: " + TimeConverter.convert(world.getTimeOfDay());
        } else {
            smallImageKey = "https://mc-heads.net/avatar/" + instance.getSession().getUuid();
            smallImageText = instance.getSession().getUsername();
        }

        return new String[]{smallImageKey, smallImageText};
    }

    public String[] getLargeStateInfo(World world, String stateString) {
        String imageText;
        String imageKey;

        if (world != null) {
            imageText = CheckWorld.worldDimension(world);
            imageKey = CheckWorld.biome(world);
        } else {
            imageText = "Main Menu";
            imageKey = "main_menu";
            if (currentScreen instanceof ConnectScreen || currentScreen instanceof LevelLoadingScreen) {
                stateString = Configurations.useBrokenEnglish ? "Connecting to a Wurld" : "Connecting to a World";
            } else {
                stateString = Configurations.useBrokenEnglish
                        ? (stateString.equals("LMC") ? "Launchin Minecraft" : instance.getSession().getUsername() + " iz Chillin")
                        : (stateString.equals("LMC") ? "Launching Minecraft" : instance.getSession().getUsername() + " is Chilling");
            }
        }

        return new String[]{imageText, imageKey, stateString};
    }

    public String checkForIdle(String StateString) {
        if (!instance.isWindowFocused()) {
            StateString = "Idling...";
        }
        return StateString;
    }

    private static class Listener implements IPCListener
    {
        @Override
        public void onReady(IPCClient client)
        {
            System.out.println("Connected to Discord ("+client.getDiscordBuild().name().toLowerCase()+")");
        }
    }
}
