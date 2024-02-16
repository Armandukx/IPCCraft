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
}
