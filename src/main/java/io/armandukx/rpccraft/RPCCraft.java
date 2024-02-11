package io.armandukx.rpccraft;

import io.armandukx.rpccraft.command.RPCCraftCommand;
import io.armandukx.rpccraft.config.Config;
import io.armandukx.rpccraft.config.Configurations;
import io.armandukx.rpccraft.config.RPCConfig;
import io.armandukx.rpccraft.utils.CheckWorld;
import io.armandukx.rpccraft.utils.DiscordPresence;
import io.armandukx.rpccraft.utils.UpdateChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class RPCCraft implements ClientModInitializer {
	public static final String VERSION = "1.0.3";
	public static final String MCVERSION = "1.16.5";
	public static final String prefix =
			Formatting.LIGHT_PURPLE + "[R" + Formatting.LIGHT_PURPLE + "P" + Formatting.LIGHT_PURPLE + "C" + Formatting.LIGHT_PURPLE + "C" + Formatting.LIGHT_PURPLE + "r" + Formatting.LIGHT_PURPLE + "a" + Formatting.LIGHT_PURPLE + "f" + Formatting.LIGHT_PURPLE + "t] " + Formatting.RESET;
	String currentScreenString = "NULL";
	MinecraftClient client = MinecraftClient.getInstance();
	private static Config config;
	private static DiscordPresence discordPresence;
	@Override
	public void onInitializeClient() {
		config = new Config();
		discordPresence = new DiscordPresence();
		UpdateChecker.init();
		Configurations.init("rpccraft", Configurations.class);

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> RPCCraftCommand.register(dispatcher));
		ChangePresence("LMC", client.world);

		ClientTickEvents.START_CLIENT_TICK.register(client -> ChangePresence("Playing Singleplayer", client.world));
		ClientLifecycleEvents.CLIENT_STOPPING.register(server -> {
			System.out.println("Saving Config");
			config.saveConfig();
			System.out.println("Stopping Discord IPC");
			discordPresence.clearPresence();
		});
	}

	public void ChangePresence(String StateString, World world){
		String imageText;
		String imageKey;

		String[] largeStateInfo = discordPresence.getLargeStateInfo(world, StateString);
		imageText = largeStateInfo[0];
		imageKey = largeStateInfo[1];
		if (!largeStateInfo[2].equals(StateString)){
			StateString = largeStateInfo[2];
		}

		if (CheckWorld.isSinglePlayer(world) && currentScreenString.equals(imageText)) {
			return;
		}

		String DetailsString= "null";

		String[] playerWorldInfo = discordPresence.getWorldInfo(world, imageText);

		if (playerWorldInfo != null && playerWorldInfo.length >= 2) {
			DetailsString = playerWorldInfo[0];

			if (playerWorldInfo.length >= 3) {
				StateString = playerWorldInfo[1];
			}

			if (playerWorldInfo.length >= 4) {
				imageText = playerWorldInfo[2];
				imageKey = playerWorldInfo[3];
			}
		}

		StateString = discordPresence.checkForIdle(StateString);

		String[] smallImageInfo = discordPresence.getSmallImageInfo(world);
		discordPresence.Update(DetailsString, StateString, imageText, imageKey, smallImageInfo[0], smallImageInfo[1]);
	}
}