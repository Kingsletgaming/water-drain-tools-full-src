package com.king.drainhelper.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class WaterPumpItem extends Item {

    public static final int RADIUS = 3; // 7×7×7 area

    public WaterPumpItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos center = context.getClickedPos();

        if (level.isClientSide() || player == null) {
            return InteractionResult.SUCCESS;
        }

        int removed = 0;

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (state.getFluidState().is(Fluids.WATER) || state.getFluidState().is(Fluids.FLOWING_WATER)) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        removed++;
                    }
                }
            }
        }

        if (removed > 0) {
            if (player instanceof ServerPlayer serverPlayer) {
                context.getItemInHand().hurtAndBreak(
                        Math.max(1, removed / 10),
                        (ServerLevel) level,
                        serverPlayer,
                        item -> {}
                );
            }

            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§bDrained " + removed + " water blocks!"),
                    true
            );
        }

        return InteractionResult.SUCCESS;
    }
}