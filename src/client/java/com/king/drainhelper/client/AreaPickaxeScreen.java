package com.king.drainhelper.client;

import com.king.drainhelper.item.ModComponents;
import com.king.drainhelper.network.PickaxeConfigPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AreaPickaxeScreen extends Screen {

    private final ItemStack pickaxe;
    private EditBox widthBox;
    private EditBox heightBox;

    public AreaPickaxeScreen(ItemStack pickaxe) {
        super(Component.literal("Area Pickaxe Configuration"));
        this.pickaxe = pickaxe.copy();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int currentWidth = pickaxe.getOrDefault(ModComponents.MINING_WIDTH, 3);
        int currentHeight = pickaxe.getOrDefault(ModComponents.MINING_HEIGHT, 3);

        // Width box
        this.widthBox = new EditBox(this.font, centerX - 60, centerY - 25, 50, 20, Component.literal("Width"));
        this.widthBox.setValue(String.valueOf(currentWidth));
        this.widthBox.setMaxLength(2);
        this.widthBox.setFilter(s -> s.matches("\\d*"));
        this.addRenderableWidget(this.widthBox);

        // Height box
        this.heightBox = new EditBox(this.font, centerX + 10, centerY - 25, 50, 20, Component.literal("Height"));
        this.heightBox.setValue(String.valueOf(currentHeight));
        this.heightBox.setMaxLength(2);
        this.heightBox.setFilter(s -> s.matches("\\d*"));
        this.addRenderableWidget(this.heightBox);

        // Apply button
        this.addRenderableWidget(
                Button.builder(Component.literal("Apply"), button -> applySettings())
                        .bounds(centerX - 50, centerY + 20, 100, 20)
                        .build()
        );

        // Close button
        this.addRenderableWidget(
                Button.builder(Component.literal("Close"), button -> this.onClose())
                        .bounds(centerX - 50, centerY + 48, 100, 20)
                        .build()
        );
    }

    private void applySettings() {
        try {
            int width = Integer.parseInt(this.widthBox.getValue());
            int height = Integer.parseInt(this.heightBox.getValue());

            width = Math.max(1, Math.min(width, 15));
            height = Math.max(1, Math.min(height, 15));

            ClientPlayNetworking.send(new PickaxeConfigPayload(width, height));
            this.onClose();
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Correct way in 1.21.11 — do NOT call renderBackground yourself
        super.render(graphics, mouseX, mouseY, partialTick);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Title
        graphics.drawCenteredString(this.font, "§6§lArea Pickaxe", centerX, centerY - 80, 0xFFFFFF);

        // Subtitle
        graphics.drawCenteredString(this.font, "§7Configure Mining Size", centerX, centerY - 65, 0xAAAAAA);

        // Labels
        graphics.drawCenteredString(this.font, "§fWidth", centerX - 35, centerY - 40, 0xFFFFFF);
        graphics.drawCenteredString(this.font, "§fHeight", centerX + 35, centerY - 40, 0xFFFFFF);

        // Help text
        graphics.drawCenteredString(this.font, "§8Min: 1   •   Max: 15", centerX, centerY + 5, 0x888888);
        graphics.drawCenteredString(this.font, "§7Example: 3 × 3  or  5 × 7", centerX, centerY + 80, 0xAAAAAA);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}