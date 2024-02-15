package io.armandukx.ipccraft;

import io.armandukx.ipccraft.config.CConfig;
import io.armandukx.ipccraft.config.IPCConfig;
import io.armandukx.ipccraft.utils.CheckWorld;
import io.armandukx.ipccraft.utils.DiscordPresence;
import io.armandukx.ipccraft.utils.UpdateChecker;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class IPCCraft implements ClientModInitializer {
	public static final String VERSION = "1.0.5";
	public static final String MCVERSION = "1.16.5";
	public static final String prefix =
			Formatting.LIGHT_PURPLE + "[I" + Formatting.LIGHT_PURPLE + "P" + Formatting.LIGHT_PURPLE + "C" + Formatting.LIGHT_PURPLE + "C" + Formatting.LIGHT_PURPLE + "r" + Formatting.LIGHT_PURPLE + "a" + Formatting.LIGHT_PURPLE + "f" + Formatting.LIGHT_PURPLE + "t] " + Formatting.RESET;
	String currentScreenString = "NULL";
	MinecraftClient client = MinecraftClient.getInstance();
	private static IPCConfig ipcConfig;
	private static DiscordPresence discordPresence;
	public static CConfig config;
	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(CConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(CConfig.class).getConfig();

		// Load Essential Stuff
		ipcConfig = new IPCConfig();
		discordPresence = new DiscordPresence();
		UpdateChecker.init();

		ChangePresence("LMC", client.world);

		ClientTickEvents.START_CLIENT_TICK.register(client -> ChangePresence("Playing Singleplayer", client.world));
		ClientLifecycleEvents.CLIENT_STOPPING.register(server -> {
			System.out.println("Saving IPCConfig");
			ipcConfig.saveConfig();
			System.out.println("Stopping Discord IPC");
			discordPresence.clearPresence();
		});
	}

	public void ChangePresence(String StateString, World world){
		if (CConfig.LayoutConfig.useCustom){
			discordPresence.Update(CConfig.LayoutConfig.detailsString, CConfig.LayoutConfig.stateString, CConfig.LayoutConfig.bigImageText, CConfig.LayoutConfig.bigImageName, null, null);
			return;
		}
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