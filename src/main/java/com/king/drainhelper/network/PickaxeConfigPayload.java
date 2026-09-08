package com.king.drainhelper.network;

import com.king.drainhelper.WaterDrainTools;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PickaxeConfigPayload(
        int width,
        int height
) implements CustomPacketPayload {

    public static final Identifier ID =
            Identifier.fromNamespaceAndPath(
                    WaterDrainTools.MOD_ID,
                    "pickaxe_config"
            );

    public static final Type<PickaxeConfigPayload> TYPE =
            new Type<>(ID);

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            PickaxeConfigPayload
            > CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            PickaxeConfigPayload::width,

            ByteBufCodecs.VAR_INT,
            PickaxeConfigPayload::height,

            PickaxeConfigPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}