package com.king.drainhelper.client;

import com.king.drainhelper.block.ModBlocks;
import com.king.drainhelper.item.ModComponents;
import com.king.drainhelper.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class WaterDrainToolsClient implements ClientModInitializer {

    private static KeyMapping OPEN_PICKAXE_MENU;

    @Override
    public void onInitializeClient() {

        // ===== Keybind (G) =====
        OPEN_PICKAXE_MENU = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.waterdrain.openmenu",
                        GLFW.GLFW_KEY_G,
                        KeyMapping.Category.MISC
                )
        );
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_PICKAXE_MENU.consumeClick()) {
                if (client.player == null) continue;

                ItemStack held = client.player.getMainHandItem();

                if (held.is(ModItems.AREA_PICKAXE) || held.is(ModItems.AREA_SHOVEL)) {
                    Minecraft.getInstance().setScreen(new AreaPickaxeScreen(held));
                }
            }
        });

        // ===== Tooltip =====
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.is(ModItems.AREA_PICKAXE)) {
                int width = stack.getOrDefault(ModComponents.MINING_WIDTH, 3);
                int height = stack.getOrDefault(ModComponents.MINING_HEIGHT, 3);

                lines.add(Component.literal("§7Mines a §e" + width + "×" + height + "§7 area"));
                lines.add(Component.literal(""));
                lines.add(Component.literal("§6Press §eG §6while holding to configure size"));
                //lines.add(Component.literal("§8Sold by Toolsmith villagers (3 Iron Ingots)"));
            }
        });
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.is(ModItems.AREA_PICKAXE) || stack.is(ModItems.AREA_SHOVEL)) {
                int width = stack.getOrDefault(ModComponents.MINING_WIDTH, 3);
                int height = stack.getOrDefault(ModComponents.MINING_HEIGHT, 3);

                lines.add(Component.literal("§7Mines a §e" + width + "×" + height + "§7 area"));
                lines.add(Component.literal(""));
                lines.add(Component.literal("§6Press §eG §6while holding to configure size"));
                //lines.add(Component.literal("§8Sold by Toolsmith villagers (3 Iron Ingots)"));
            }
        });

        // ===== Tooltips =====
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {

            // Area Pickaxe
            if (stack.is(ModItems.AREA_PICKAXE)) {
                int width = stack.getOrDefault(ModComponents.MINING_WIDTH, 3);
                int height = stack.getOrDefault(ModComponents.MINING_HEIGHT, 3);

                lines.add(Component.literal("§7Mines a §e" + width + "×" + height + "§7 area"));
                lines.add(Component.literal("§6Press §eG §6to configure size"));
                lines.add(Component.literal("§8Sold by Toolsmith (3 Iron Ingots)"));
            }

            // Area Shovel
            if (stack.is(ModItems.AREA_SHOVEL)) {
                int width = stack.getOrDefault(ModComponents.MINING_WIDTH, 3);
                int height = stack.getOrDefault(ModComponents.MINING_HEIGHT, 3);

                lines.add(Component.literal("§7Digs a §e" + width + "×" + height + "§7 area of soft blocks"));
                lines.add(Component.literal("§6Press §eG §6to configure size"));
                lines.add(Component.literal("§8Great for dirt, sand, gravel..."));
            }

            // Water Channel Tool
            if (stack.is(ModItems.WATER_CHANNEL_TOOL)) {
                lines.add(Component.literal("§bCanal Builder"));
                lines.add(Component.literal("§7Right-click a block to build a long canal"));
                lines.add(Component.literal("§7Places smooth stone slabs in a straight line"));
                lines.add(Component.literal("§8Perfect for guiding water to your chamber"));
            }

            // Water Pump
            if (stack.is(ModItems.WATER_PUMP)) {
                lines.add(Component.literal("§bWater Pump / Drain"));
                lines.add(Component.literal("§7Right-click to remove water in a 7×7×7 area"));
                lines.add(Component.literal("§7Useful for emergency draining"));
                lines.add(Component.literal("§8Has durability based on water removed"));
            }

            // Water Sensor
            if (stack.is(ModBlocks.WATER_SENSOR.asItem())) {   // ← Note: this is a BlockItem
                lines.add(Component.literal("§bWater Sensor Alarm"));
                lines.add(Component.literal("§7Detects nearby water and outputs redstone"));
                lines.add(Component.literal("§7Plays a warning sound when water is detected"));
                lines.add(Component.literal("§8Place near important areas for safety"));
            }
        });
    }
}