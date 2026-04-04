package com.old_animation.mixin.client;

import com.old_animation.DamageRecordHandler;
import com.old_animation.client.gui.HitMarkerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleDamageEvent", at = @At("HEAD"))
    private void onDamageEvent(ClientboundDamageEventPacket packet, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.player != null) {
            Entity target = client.level.getEntity(packet.entityId());
            if (target instanceof LivingEntity && packet.sourceCauseId() == client.player.getId()) {
                Entity source = client.level.getEntity(packet.sourceDirectId());
                boolean isRanged = source instanceof Projectile;
                float attackStrength = client.player.getAttackStrengthScale(0.5f);
                float fakeDamage = 2.0f + (attackStrength * 7.0f);
                client.execute(() -> {
                    HitMarkerRenderer.getInstance().onHit(isRanged);
                    DamageRecordHandler.showDamage(target, fakeDamage, isRanged);
                });
            }
        }
    }
}