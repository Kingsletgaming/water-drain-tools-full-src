package com.king.drainhelper;

import com.king.drainhelper.block.ModBlocks;
import com.king.drainhelper.item.ModComponents;
import com.king.drainhelper.item.ModItems;
import com.king.drainhelper.network.PickaxeConfigPayload;
import com.king.drainhelper.sound.ModSounds;
import com.king.drainhelper.village.ModTrades;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class WaterDrainTools implements ModInitializer {

    public static final String MOD_ID = "water-drain-tools";


    @Override
    public void onInitialize() {
        ModComponents.initialize();
        ModItems.initialize();
        ModTrades.initialize();
        ModBlocks.initialize();
        ModSounds.initialize();

        PayloadTypeRegistry.playC2S().register(
                PickaxeConfigPayload.TYPE,
                PickaxeConfigPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                PickaxeConfigPayload.TYPE,
                (payload, context) -> {
                    int width = Math.max(1, Math.min(payload.width(), 15));
                    int height = Math.max(1, Math.min(payload.height(), 15));

                    context.player().getMainHandItem().set(ModComponents.MINING_WIDTH, width);
                    context.player().getMainHandItem().set(ModComponents.MINING_HEIGHT, height);
                }
        );
    }
}