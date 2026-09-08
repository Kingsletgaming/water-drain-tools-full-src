package com.king.drainhelper.item;

import com.king.drainhelper.WaterDrainTools;
import com.king.drainhelper.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

import java.util.function.Function;

public class ModItems {

    // Built on the vanilla IRON tool material, so it mines the same blocks
    // an iron pickaxe would, at iron speed/durability. Swap for
    // ToolMaterial.DIAMOND or ToolMaterial.NETHERITE if you want it stronger.
    public static final Item AREA_PICKAXE = register(
            "area_pickaxe",
            props -> new AreaPickaxeItem(props),
            new Item.Properties().pickaxe(ToolMaterial.IRON, 1.0F, -2.8F)
    );

    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(WaterDrainTools.MOD_ID, name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }
    public static final Item AREA_SHOVEL = register(
            "area_shovel",
            props -> new AreaShovelItem(props),
            new Item.Properties().shovel(ToolMaterial.IRON, 1.5F, -3.0F)
    );

    public static final Item WATER_CHANNEL_TOOL = register(
            "water_channel_tool",
            props -> new WaterChannelToolItem(props),
            new Item.Properties().durability(250)
    );

    public static final Item WATER_PUMP = register(
            "water_pump",
            props -> new WaterPumpItem(props),
            new Item.Properties().durability(150)
    );

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> {
                    entries.accept(AREA_PICKAXE);
                    entries.accept(AREA_SHOVEL);
                    entries.accept(WATER_CHANNEL_TOOL);
                    entries.accept(WATER_PUMP);
                    entries.accept(ModBlocks.WATER_SENSOR);
                });
    }
}
