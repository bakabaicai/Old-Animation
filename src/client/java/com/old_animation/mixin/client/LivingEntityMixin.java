package com.old_animation.mixin.client;

import com.old_animation.AnimationConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "isUsingItem", at = @At("HEAD"), cancellable = true)
    private void trickIsUsingItemForSwing(CallbackInfoReturnable<Boolean> cir) {
        if (AnimationConfig.useSwing && (Object) this instanceof Player) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement element : stackTrace) {
                String methodName = element.getMethodName();
                if (methodName.equals("swing") || methodName.equals("m_6674_") ||
                        methodName.contains("attack") || methodName.contains("handleAttack")) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}