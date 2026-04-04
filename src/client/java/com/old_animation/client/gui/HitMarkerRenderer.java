package com.old_animation.client.gui;

import com.old_animation.AnimationConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;

public class HitMarkerRenderer {
    private static final HitMarkerRenderer INSTANCE = new HitMarkerRenderer();
    private long hitTime = 0;
    private static final long DURATION = 250;

    public static HitMarkerRenderer getInstance() {
        return INSTANCE;
    }

    public void onHit(boolean isRanged) {
        if (AnimationConfig.hitMarker) {
            this.hitTime = System.currentTimeMillis();

            if (AnimationConfig.hitSound) {
                boolean shouldPlay = false;
                if (AnimationConfig.hitSoundCondition == AnimationConfig.HitSoundCondition.BOTH) {
                    shouldPlay = true;
                } else if (AnimationConfig.hitSoundCondition == AnimationConfig.HitSoundCondition.MELEE && !isRanged) {
                    shouldPlay = true;
                } else if (AnimationConfig.hitSoundCondition == AnimationConfig.HitSoundCondition.RANGED && isRanged) {
                    shouldPlay = true;
                }

                if (shouldPlay) {
                    Minecraft client = Minecraft.getInstance();
                    if (client.player != null) {
                        if (AnimationConfig.hitSoundType == AnimationConfig.HitSoundType.NETHERITE) {
                            client.player.playSound(SoundEvents.NETHERITE_BLOCK_HIT, 1.0F, 0.9F);
                        } else {
                            client.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 0.5F);
                        }
                    }
                }
            }
        }
    }

    public void render(GuiGraphics graphics) {
        if (!AnimationConfig.hitMarker || hitTime == 0) return;

        long elapsed = System.currentTimeMillis() - hitTime;
        if (elapsed > DURATION) {
            hitTime = 0;
            return;
        }

        float alphaProgress = 1.0f - (float) elapsed / DURATION;
        int alpha = (int) (alphaProgress * 255);
        int color = (alpha << 24) | 0xFFFFFF;

        Minecraft client = Minecraft.getInstance();
        int centerX = client.getWindow().getGuiScaledWidth() / 2;
        int centerY = client.getWindow().getGuiScaledHeight() / 2;

        int length = 5;
        int gap = 3;

        drawDiagonal(graphics, centerX - gap, centerY - gap, -1, -1, length, color);
        drawDiagonal(graphics, centerX + gap, centerY - gap, 1, -1, length, color);
        drawDiagonal(graphics, centerX - gap, centerY + gap, -1, 1, length, color);
        drawDiagonal(graphics, centerX + gap, centerY + gap, 1, 1, length, color);
    }

    private void drawDiagonal(GuiGraphics graphics, int x, int y, int dx, int dy, int len, int color) {
        for (int i = 0; i < len; i++) {
            graphics.fill(x + (i * dx), y + (i * dy), x + (i * dx) + 1, y + (i * dy) + 1, color);
        }
    }
}