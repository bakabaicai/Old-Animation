package com.old_animation;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.network.chat.Component;

public class OldAnimationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AnimationConfig.load();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("oldanimation")
                    .then(ClientCommandManager.literal("auto")
                            .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                                    .executes(context -> {
                                        AnimationConfig.autoMode = BoolArgumentType.getBool(context, "enabled");
                                        AnimationConfig.save();
                                        context.getSource().sendFeedback(Component.literal("Auto mode: " + AnimationConfig.autoMode));
                                        return 1;
                                    })))
                    .then(ClientCommandManager.literal("range")
                            .then(ClientCommandManager.argument("value", DoubleArgumentType.doubleArg(0, 10))
                                    .executes(context -> {
                                        AnimationConfig.range = DoubleArgumentType.getDouble(context, "value");
                                        AnimationConfig.save();
                                        context.getSource().sendFeedback(Component.literal("Range set to: " + AnimationConfig.range));
                                        return 1;
                                    })))
            );
        });
    }
}