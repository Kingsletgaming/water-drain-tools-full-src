package com.king.drainhelper.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AreaShovelItem extends Item {

    public AreaShovelItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        super.mineBlock(stack, level, state, pos, miner);

        if (level.isClientSide()) {
            return true;
        }

        if (!(miner instanceof Player player)) {
            return true;
        }

        if (stack.isEmpty()) {
            return true;
        }

        int width = stack.getOrDefault(ModComponents.MINING_WIDTH, 3);
        int height = stack.getOrDefault(ModComponents.MINING_HEIGHT, 3);

        width = Math.max(1, Math.min(width, 15));
        height = Math.max(1, Math.min(height, 15));

        Direction face = getFacingAxis(player);

        for (BlockPos extraPos : getPlanePositions(pos, face, width, height)) {
            if (stack.isEmpty()) break;

            BlockState extraState = level.getBlockState(extraPos);

            if (extraState.isAir()) continue;
            if (extraState.getDestroySpeed(level, extraPos) < 0) continue;

            // Only mine blocks that a shovel is the correct tool for
            if (!stack.isCorrectToolForDrops(extraState)) continue;

            level.destroyBlock(extraPos, true, player);
            damageShovel(stack, player);

            if (stack.isEmpty()) break;
        }

        return true;
    }

    private void damageShovel(ItemStack stack, Player player) {
        int newDamage = stack.getDamageValue() + 1;
        if (newDamage >= stack.getMaxDamage()) {
            stack.shrink(1);
        } else {
            stack.setDamageValue(newDamage);
        }
    }

    private Direction getFacingAxis(Player player) {
        Vec3 look = player.getLookAngle();
        double absX = Math.abs(look.x);
        double absY = Math.abs(look.y);
        double absZ = Math.abs(look.z);

        if (absY > absX && absY > absZ) {
            return look.y > 0 ? Direction.UP : Direction.DOWN;
        } else if (absX > absZ) {
            return look.x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return look.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    private List<BlockPos> getPlanePositions(BlockPos center, Direction face, int width, int height) {
        List<BlockPos> positions = new ArrayList<>();
        Direction.Axis axis = face.getAxis();

        int widthStart = -(width / 2);
        int widthEnd = widthStart + width - 1;
        int heightStart = -(height / 2);
        int heightEnd = heightStart + height - 1;

        for (int a = widthStart; a <= widthEnd; a++) {
            for (int b = heightStart; b <= heightEnd; b++) {
                if (a == 0 && b == 0) continue;

                BlockPos blockPos = switch (axis) {
                    case X -> center.offset(0, b, a);
                    case Y -> center.offset(a, 0, b);
                    case Z -> center.offset(a, b, 0);
                    default -> center;
                };
                positions.add(blockPos);
            }
        }
        return positions;
    }
}