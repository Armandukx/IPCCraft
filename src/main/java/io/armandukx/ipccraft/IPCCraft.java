package io.armandukx.ipccraft;

import io.armandukx.ipccraft.config.ClothConfig;
import io.armandukx.ipccraft.config.IPCConfig;
import io.armandukx.ipccraft.utils.CheckWorld;
import io.armandukx.ipccraft.utils.DiscordPresence;
import io.armandukx.ipccraft.utils.Screenshot;
import io.armandukx.ipccraft.utils.UpdateChecker;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class IPCCraft implements ClientModInitializer {
	public static final String VERSION = "1.0.10";
	public static final String MCVERSION = MinecraftVersion.create().getName();
	public static final String prefix = Formatting.LIGHT_PURPLE + "[I" + Formatting.LIGHT_PURPLE + "P" + Formatting.LIGHT_PURPLE + "C" + Formatting.LIGHT_PURPLE + "C" + Formatting.LIGHT_PURPLE + "r" + Formatting.LIGHT_PURPLE + "a" + Formatting.LIGHT_PURPLE + "f" + Formatting.LIGHT_PURPLE + "t] " + Formatting.RESET;
	String currentScreenString = "NULL";
	MinecraftClient client = MinecraftClient.getInstance();
	private static IPCConfig config;
	private static DiscordPresence discordPresence;
	public static boolean _STOPCHECKING = false;
	private boolean _STOP = false;
	@Override
	public void onInitializeClient() {
		// Config
		config = new IPCConfig();
		AutoConfig.register(ClothConfig.class, GsonConfigSerializer::new);
		AutoConfig.getConfigHolder(ClothConfig.class).getConfig();

		discordPresence = new DiscordPresence();
		Screenshot.initScreenshot();
		ChangePresence("LMC", client.world);

		ClientLifecycleEvents.CLIENT_STOPPING.register(server -> {
			_STOP = true;
			System.out.println("Saving IPCConfig");
			config.saveConfig();
			System.out.println("Stopping Discord IPC");
			discordPresence.clearPresence();
		});
		ClientTickEvents.END_CLIENT_TICK.register(server -> {
			if (_STOP){discordPresence.clearPresence();
				return;}
			ChangePresence("Playing Singleplayer", client.world);
			if (!_STOPCHECKING){
				UpdateChecker.check();
			}
		});
	}

	public void ChangePresence(String StateString, World world){
		if (ClothConfig.CustomPresence.useCustom){
			discordPresence.Update(ClothConfig.CustomPresence.detailsString, ClothConfig.CustomPresence.stateString, ClothConfig.CustomPresence.bigImageText, ClothConfig.CustomPresence.bigImageName, ClothConfig.CustomPresence.smallImageName, ClothConfig.CustomPresence.smallImageText);
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

		String[] playerWorldInfo = discordPresence.getWorldInfo(world, imageText, StateString);

		if (playerWorldInfo != null && playerWorldInfo.length >= 2) {
			DetailsString = playerWorldInfo[0];
			StateString = playerWorldInfo[1];

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