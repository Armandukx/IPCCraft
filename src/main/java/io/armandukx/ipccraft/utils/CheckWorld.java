package io.armandukx.ipccraft.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class CheckWorld {
    static MinecraftClient client = MinecraftClient.getInstance();
    public static boolean isSinglePlayer(World world) {
        if (world != null && client.isIntegratedServerRunning()) {
            return true;
        }
        else return world == null;
    }

    public static String worldDimension(World world){
        if (world == null) return "NULL";
        RegistryKey<World> dimensionKey = world.getRegistryKey();

        if (dimensionKey == World.OVERWORLD) {
            return "Overworld";
        } else if (dimensionKey == World.NETHER) {
            return "Nether";
        } else {
            return "End";
        }
    }

    public static String biome(World world){
        if (world == null) return "NULL";
        String biome = world.getBiome(client.player.getBlockPos()).getCategory().name().toLowerCase();

        if (biome.equals("plains") || biome.equals("forest")){
            return "forest";
        } else if (biome.contains("ocean")) {
            return "ocean";
        } else if (biome.contains("savanna")) {
            return "savanna";
        }else if (biome.contains("dessert")) {
            return "dessert";
        }else if (biome.contains("river")) {
            return "river";
        }else if (biome.contains("taiga")) {
            return "taiga";
        }else if (biome.contains("beach")) {
            return "beach";
        }else if (biome.contains("mushroom")) {
            return "mushroom";
        }else if (biome.contains("swamp")) {
            return "swamp";
        }
        return worldDimension(world).toLowerCase();
    }
}
