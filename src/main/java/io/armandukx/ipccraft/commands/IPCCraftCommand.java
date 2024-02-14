package io.armandukx.ipccraft.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.armandukx.ipccraft.config.Configurations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class IPCCraftCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("ipccraft")
                .executes(context -> {
                    MinecraftClient.getInstance().openScreen(new Configurations().getScreen(null));
                    return 1;
                });
        dispatcher.register(builder);
    }
}