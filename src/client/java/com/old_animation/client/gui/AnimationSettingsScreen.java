package com.old_animation.client.gui;

import com.old_animation.AnimationConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import java.util.Locale;

public class AnimationSettingsScreen extends Screen {
    private final Screen lastScreen;

    public AnimationSettingsScreen(Screen lastScreen) {
        super(Component.literal(AnimationConfig.isChinese ? "OldAnimation 设置" : "OldAnimation Settings"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        boolean cn = AnimationConfig.isChinese;

        String modePrefix = cn ? "动画模式: " : "Animation Mode: ";
        this.addRenderableWidget(Button.builder(
                Component.literal(modePrefix + getModeName()),
                (button) -> {
                    cycleMode();
                    button.setMessage(Component.literal(modePrefix + getModeName()));
                    AnimationConfig.save();
                }).bounds(centerX - 75, centerY - 110, 150, 20).build());

        this.addRenderableWidget(new AbstractSliderButton(centerX - 75, centerY - 85, 150, 20, Component.empty(), (AnimationConfig.offsetX + 1f) / 2f) {
            { updateMessage(); }
            @Override protected void updateMessage() { this.setMessage(Component.literal("X: " + String.format(Locale.ROOT, "%.2f", AnimationConfig.offsetX))); }
            @Override protected void applyValue() { AnimationConfig.offsetX = (float) (this.value * 2.0 - 1.0); AnimationConfig.save(); }
        });

        this.addRenderableWidget(new AbstractSliderButton(centerX - 75, centerY - 60, 150, 20, Component.empty(), (AnimationConfig.offsetY + 1f) / 2f) {
            { updateMessage(); }
            @Override protected void updateMessage() { this.setMessage(Component.literal("Y: " + String.format(Locale.ROOT, "%.2f", AnimationConfig.offsetY))); }
            @Override protected void applyValue() { AnimationConfig.offsetY = (float) (this.value * 2.0 - 1.0); AnimationConfig.save(); }
        });

        this.addRenderableWidget(new AbstractSliderButton(centerX - 75, centerY - 35, 150, 20, Component.empty(), (AnimationConfig.offsetZ + 1f) / 2f) {
            { updateMessage(); }
            @Override protected void updateMessage() { this.setMessage(Component.literal("Z: " + String.format(Locale.ROOT, "%.2f", AnimationConfig.offsetZ))); }
            @Override protected void applyValue() { AnimationConfig.offsetZ = (float) (this.value * 2.0 - 1.0); AnimationConfig.save(); }
        });

        String speedName = cn ? "动画速度" : "Animation Speed";
        this.addRenderableWidget(new AbstractSliderButton(centerX - 75, centerY - 10, 150, 20, Component.empty(), AnimationConfig.animSpeed / 4.0f) {
            { updateMessage(); }
            @Override protected void updateMessage() { this.setMessage(Component.literal(speedName + ": " + String.format(Locale.ROOT, "%.2f", AnimationConfig.animSpeed))); }
            @Override protected void applyValue() { AnimationConfig.animSpeed = (float) (this.value * 4.0); AnimationConfig.save(); }
        });

        String reachName = cn ? "触发距离" : "Reach";
        this.addRenderableWidget(new AbstractSliderButton(centerX - 75, centerY + 15, 150, 20, Component.empty(), (float)((AnimationConfig.range - 2.0) / 4.0)) {
            { updateMessage(); }
            @Override protected void updateMessage() { this.setMessage(Component.literal(reachName + ": " + String.format(Locale.ROOT, "%.2f", AnimationConfig.range))); }
            @Override protected void applyValue() { AnimationConfig.range = 2.0 + (this.value * 4.0); AnimationConfig.save(); }
        });

        int btnW = 85;
        int sX = centerX - (btnW * 3 + 4) / 2;

        this.addRenderableWidget(Button.builder(
                Component.literal(getToggleText(cn ? "格挡动画" : "Sword Block", AnimationConfig.swordBlock, cn)),
                (button) -> {
                    AnimationConfig.swordBlock = !AnimationConfig.swordBlock;
                    button.setMessage(Component.literal(getToggleText(cn ? "格挡动画" : "Sword Block", AnimationConfig.swordBlock, cn)));
                    AnimationConfig.save();
                }).bounds(sX, centerY + 40, btnW, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal(getToggleText(cn ? "使用动画" : "UseSwing", AnimationConfig.useSwing, cn)),
                (button) -> {
                    AnimationConfig.useSwing = !AnimationConfig.useSwing;
                    button.setMessage(Component.literal(getToggleText(cn ? "使用动画" : "UseSwing", AnimationConfig.useSwing, cn)));
                    AnimationConfig.save();
                }).bounds(sX + btnW + 2, centerY + 40, btnW, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal(getToggleText(cn ? "自动格挡" : "Auto", AnimationConfig.autoMode, cn)),
                (button) -> {
                    AnimationConfig.autoMode = !AnimationConfig.autoMode;
                    button.setMessage(Component.literal(getToggleText(cn ? "自动格挡" : "Auto", AnimationConfig.autoMode, cn)));
                    AnimationConfig.save();
                }).bounds(sX + (btnW + 2) * 2, centerY + 40, btnW, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal(cn ? "\u00A7c重置所有设置" : "\u00A7cReset All Settings"),
                (button) -> {
                    AnimationConfig.offsetX = 0.0f;
                    AnimationConfig.offsetY = 0.0f;
                    AnimationConfig.offsetZ = 0.0f;
                    AnimationConfig.animSpeed = 1.0f;
                    AnimationConfig.range = 3.0;
                    AnimationConfig.swordBlock = false;
                    AnimationConfig.useSwing = false;
                    AnimationConfig.autoMode = false;
                    AnimationConfig.animationMode = AnimationConfig.AnimMode.MODE_1_7;
                    AnimationConfig.save();
                    if (this.minecraft != null) this.minecraft.setScreen(new AnimationSettingsScreen(this.lastScreen));
                }).bounds(5, this.height - 75, 90, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal(cn ? "\u00A7a作者主页" : "\u00A7aAuthor"),
                (button) -> {
                    if (this.minecraft != null) {
                        String url = "https://space.bilibili.com/3546915648047958";
                        this.minecraft.setScreen(new net.minecraft.client.gui.screens.ConfirmLinkScreen((confirmed) -> {
                            if (confirmed) {
                                Util.getPlatform().openUri(url);
                            }
                            this.minecraft.setScreen(this);
                        }, url, true));
                    }
                }).bounds(5, this.height - 50, 90, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(cn ? "Language: CN" : "Language: EN"), (button) -> {
            AnimationConfig.isChinese = !AnimationConfig.isChinese;
            AnimationConfig.save();
            if (this.minecraft != null) this.minecraft.setScreen(new AnimationSettingsScreen(this.lastScreen));
        }).bounds(5, this.height - 25, 90, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(cn ? "完成" : "Done"), (button) -> this.onClose())
                .bounds(centerX - 75, centerY + 65, 150, 20).build());
    }

    private String getToggleText(String prefix, boolean value, boolean isChinese) {
        if (isChinese) {
            return prefix + ": " + (value ? "开" : "关");
        } else {
            return prefix + ": " + (value ? "ON" : "OFF");
        }
    }

    private String getModeName() {
        return switch (AnimationConfig.animationMode) {
            case MODE_1_7 -> "1.7";
            case MODE_PUSH -> "Push";
            case MODE_1_7_PLUS -> "1.7+";
        };
    }

    private void cycleMode() {
        AnimationConfig.animationMode = switch (AnimationConfig.animationMode) {
            case MODE_1_7 -> AnimationConfig.AnimMode.MODE_PUSH;
            case MODE_PUSH -> AnimationConfig.AnimMode.MODE_1_7_PLUS;
            case MODE_1_7_PLUS -> AnimationConfig.AnimMode.MODE_1_7;
        };
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.setScreen(this.lastScreen);
    }
}