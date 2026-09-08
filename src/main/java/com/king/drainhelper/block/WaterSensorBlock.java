package com.king.drainhelper.block;

import com.king.drainhelper.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;

public class WaterSensorBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public WaterSensorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 10);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean waterDetected = false;

        // Detect water in 5x5x5 area
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 3; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (level.getFluidState(pos.offset(x, y, z)).is(Fluids.WATER)) {
                        waterDetected = true;
                        break;
                    }
                }
                if (waterDetected) break;
            }
            if (waterDetected) break;
        }

        boolean currentlyPowered = state.getValue(POWERED);

        if (waterDetected) {
            // Keep it powered
            if (!currentlyPowered) {
                level.setBlock(pos, state.setValue(POWERED, true), 3);
            }

            // Play real tornado siren
            level.playSound(
                    null,
                    pos,
                    ModSounds.TORNADO_SIREN,
                    SoundSource.BLOCKS,
                    2.0f,    // Very loud
                    1.0f
            );

        } else if (currentlyPowered) {
            // Turn off when water is gone
            level.setBlock(pos, state.setValue(POWERED, false), 3);
        }

        // Check again very soon (for continuous alarm)
        level.scheduleTick(pos, this, 15); // every 0.75 seconds
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }
}