package io.armandukx.rpccraft;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class RPCCraft implements ClientModInitializer {
	public static final String VERSION = "1.0.1";
	String currentScreenString = "NULL";
	MinecraftClient client = MinecraftClient.getInstance();
	@Override
	public void onInitializeClient() {
		DiscordRPC lib = DiscordRPC.INSTANCE;
		lib.Discord_Initialize("1200554143874555935", new DiscordEventHandlers(), true, "");
		DiscordRichPresence presence = new DiscordRichPresence();
		presence.startTimestamp = System.currentTimeMillis() / 1000;

		ClientTickEvents.START_CLIENT_TICK.register(client -> ChangePresence("Playing Singleplayer", client.world, lib, presence));
	}

	public void ChangePresence(String StateString, World world, DiscordRPC lib, DiscordRichPresence presence){
		Screen currentScreen = client.currentScreen;

		String imageText = "";
		String imageKey = "";

		if (world != null) {
			RegistryKey<World> dimensionKey = world.getRegistryKey();

			if (dimensionKey == World.OVERWORLD) {
				imageText = "Overworld";
				imageKey = "overworld";
			} else if (dimensionKey == World.NETHER) {
				imageText = "Nether";
				imageKey = "nether";
			} else if (dimensionKey == World.END) {
				imageText = "End";
				imageKey = "end";
			}
		} else {
			imageText = "Main Menu";
			imageKey = "main_menu";
			if (currentScreen instanceof ConnectScreen) {
				StateString = "Connecting to a server";
			} else {
				StateString = "Chilling";
			}
		}

		if (isSinglePlayer(world) && currentScreenString.equals(imageText)) {
			return;
		}

		String DetailsString = currentScreenString.equals("Main Menu") ? imageText : "NULL";

		if (isSinglePlayer(world)) {
			DetailsString = "Currently In The " + imageText;
		}else {
			ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
			if (networkHandler != null) {
				String ServerAddress = networkHandler.getConnection().getAddress().toString().split("[/\\\\]")[0];
				int playerCount = networkHandler.getPlayerList().size();
				DetailsString = "Playing with " + playerCount + " Minecrafters";
				StateString = "Playing Multiplayer";
				imageText = ServerAddress;
				imageKey = "https://eu.mc-api.net/v3/server/favicon/" + ServerAddress;
			}
		}

		presence.details = DetailsString;
		presence.state = StateString;
		presence.largeImageText = imageText;
		presence.largeImageKey = imageKey;
		lib.Discord_UpdatePresence(presence);
	}
	public boolean isSinglePlayer(World world) {
		if (world != null && client.isIntegratedServerRunning()) {
			return true;
		}
		else return world == null;
	}
}