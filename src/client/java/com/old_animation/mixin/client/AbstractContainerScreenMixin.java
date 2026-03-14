package com.old_animation.mixin.client;

import com.old_animation.client.gui.AnimationSettingsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {
    protected AbstractContainerScreenMixin(Component title) { super(title); }

    @Inject(method = "init", at = @At("TAIL"))
    private void addGlobalSettingsButton(CallbackInfo ci) {
        this.addRenderableWidget(Button.builder(Component.literal("OldAnimation"), (button) -> {
            if (this.minecraft != null) this.minecraft.setScreen(new AnimationSettingsScreen(this));
        }).bounds(this.width - 82, 2, 80, 20).build());
    }
}