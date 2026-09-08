package com.king.drainhelper.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WaterChannelToolItem extends Item {

    public static final int MAX_LENGTH = 32;

    public WaterChannelToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos start = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();

        if (level.isClientSide() || player == null) {
            return InteractionResult.SUCCESS;
        }

        BlockPos current = start.relative(context.getClickedFace());

        for (int i = 0; i < MAX_LENGTH; i++) {
            BlockState state = level.getBlockState(current);

            if (state.getDestroySpeed(level, current) < 0) {
                break;
            }

            if (!state.isAir() && !state.liquid()) {
                level.destroyBlock(current, true, player);
            }

            level.setBlock(current, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3);

            current = current.relative(facing);
        }

        // Correct way to damage the item in 1.21.11
        if (player instanceof ServerPlayer serverPlayer) {
            context.getItemInHand().hurtAndBreak(1, (ServerLevel) level, serverPlayer, item -> {});
        }

        return InteractionResult.SUCCESS;
    }
}