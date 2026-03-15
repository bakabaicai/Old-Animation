package com.old_animation.mixin.client;

import com.old_animation.AnimationConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public int swingTime;

    @Inject(method = "getCurrentSwingDuration", at = @At("RETURN"), cancellable = true)
    private void modifySwingDuration(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this instanceof Player) {
            int original = cir.getReturnValue();
            cir.setReturnValue((int) (original / AnimationConfig.animSpeed));
        }
    }

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"), cancellable = true)
    private void preventSwingReset(CallbackInfo ci) {
        if ((Object) this instanceof Player && this.swingTime > 0) {
            ci.cancel();
        }
    }

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