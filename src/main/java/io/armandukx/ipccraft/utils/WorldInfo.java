package io.armandukx.ipccraft.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;

public class WorldInfo {
    static MinecraftClient client = MinecraftClient.getInstance();
    public static String returnBiome(World world){
        if (world!= null&&world.getBiome(client.player.getBlockPos()).getKey().isPresent()) {
            return world.getBiome(client.player.getBlockPos()).getKey().get().getValue().toString().toLowerCase().substring(10).replace("_", " ");
        }
        return "UNKNOWN";
    }
}
