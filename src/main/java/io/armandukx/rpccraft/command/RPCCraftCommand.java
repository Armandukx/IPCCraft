package io.armandukx.rpccraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.armandukx.rpccraft.config.Configurations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class RPCCraftCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("rpccraft")
                .then(
                        LiteralArgumentBuilder.<ServerCommandSource>literal("Config")
                                .executes((command) -> {
                                    MinecraftClient.getInstance().openScreen(new Configurations().getScreen(null));
                                    return 1;
                                })
                );
        dispatcher.register(builder);
    }
}