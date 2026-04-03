package com.old_animation.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class NotificationOverlay {
    private static final NotificationOverlay INSTANCE = new NotificationOverlay();
    private static final int HEIGHT = 55;
    private static final int GREEN_BAR_WIDTH = 6;
    private static final int PADDING = 80;
    private static final long GREEN_ANIM_DURATION = 350;
    private static final long BLACK_ANIM_DURATION = 350;
    private static final long STAY_DURATION = 2000;
    private static final int GREEN_COLOR = 0x76B900;
    private static final int BLACK_COLOR = 0x1A1A1A;

    private boolean active = false;
    private long startTime = 0;
    private String message = "";
    private int textColor = 0xFFFFFF;

    public static NotificationOverlay getInstance() {
        return INSTANCE;
    }

    public void show(String text, int color) {
        this.message = text;
        this.textColor = color;
        this.active = true;
        this.startTime = System.currentTimeMillis();
    }

    public void show(String text) {
        show(text, 0xFFFFFF);
    }

    public void render(GuiGraphics graphics) {
        if (!active) return;

        long elapsed = System.currentTimeMillis() - startTime;
        long totalAnimIn = GREEN_ANIM_DURATION + BLACK_ANIM_DURATION;
        long totalAnimOut = BLACK_ANIM_DURATION + GREEN_ANIM_DURATION;
        long totalTime = totalAnimIn + STAY_DURATION + totalAnimOut;

        if (elapsed >= totalTime) {
            active = false;
            return;
        }

        Minecraft client = Minecraft.getInstance();
        int screenWidth = client.getWindow().getGuiScaledWidth();
        int screenHeight = client.getWindow().getGuiScaledHeight();

        int textWidth = client.font.width(message);
        int dynamicWidth = textWidth + PADDING + GREEN_BAR_WIDTH;
        int y = (int) (screenHeight * 0.12);

        float greenProgress = 0f;
        float blackProgress = 0f;
        boolean textVisible = false;

        if (elapsed < GREEN_ANIM_DURATION) {
            greenProgress = easeOutPow3((float) elapsed / GREEN_ANIM_DURATION);
        } else if (elapsed < totalAnimIn) {
            greenProgress = 1f;
            blackProgress = easeOutPow3((float) (elapsed - GREEN_ANIM_DURATION) / BLACK_ANIM_DURATION);
        } else if (elapsed < totalAnimIn + STAY_DURATION) {
            greenProgress = 1f;
            blackProgress = 1f;
            textVisible = true;
        } else if (elapsed < totalAnimIn + STAY_DURATION + BLACK_ANIM_DURATION) {
            greenProgress = 1f;
            blackProgress = 1f - easeInPow3((float) (elapsed - totalAnimIn - STAY_DURATION) / BLACK_ANIM_DURATION);
            textVisible = false;
        } else {
            greenProgress = 1f - easeInPow3((float) (elapsed - totalAnimIn - STAY_DURATION - BLACK_ANIM_DURATION) / GREEN_ANIM_DURATION);
        }

        int greenX = (int) (screenWidth - (dynamicWidth * greenProgress));
        int greenAlpha = (int) (greenProgress * 255) << 24;
        graphics.fill(greenX, y, screenWidth, y + HEIGHT, greenAlpha | GREEN_COLOR);

        int blackX = (int) (screenWidth - ((dynamicWidth - GREEN_BAR_WIDTH) * blackProgress));
        int blackAlpha = (int) (blackProgress * 255) << 24;
        graphics.fill(blackX, y, screenWidth, y + HEIGHT, blackAlpha | BLACK_COLOR);

        if (textVisible) {
            int textX = blackX + (dynamicWidth - GREEN_BAR_WIDTH - textWidth) / 2;
            int textY = y + (HEIGHT - 8) / 2;
            graphics.drawString(client.font, message, textX, textY, textColor | 0xFF000000, false);
        }
    }

    private float easeOutPow3(float value) {
        return 1.0f - (float) Math.pow(1.0 - value, 3);
    }

    private float easeInPow3(float value) {
        return (float) Math.pow(value, 3);
    }
}