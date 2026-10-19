package com.sigmaspores.block;

import com.sigmaspores.init.ModBlocks;
import com.sigmaspores.init.ModGameRules;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class InfectedMassBlock extends Block {
    protected final int attempts;

    public InfectedMassBlock(Properties props, int attempts) {
        super(props);
        this.attempts = attempts;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (!world.getGameRules().getBoolean(ModGameRules.SPREAD)) return;
        if (!ModGameRules.SPREAD_ENABLED) return;
        for (int i = 0; i < attempts; i++) {
            BlockPos target = pos.offset(rand.nextInt(3) - 1, rand.nextInt(3) - 1, rand.nextInt(3) - 1);
            tryInfect(world, target, rand);
        }
    }

    /** Превращает блок в заражённую массу, если это разрешено. */
    public static boolean tryInfect(ServerWorld world, BlockPos pos, Random rand) {
        if (!world.isLoaded(pos)) return false;
        BlockState s = world.getBlockState(pos);
        if (s.isAir(world, pos)) return false;
        if (s.getBlock() instanceof InfectedMassBlock) return false;
        if (!s.getFluidState().isEmpty()) return false;
        if (s.hasTileEntity()) return false;
        float hardness = s.getDestroySpeed(world, pos);
        if (hardness < 0 || hardness > 20) return false;
        world.setBlock(pos, ModBlocks.INFECTED_MASS.get().defaultBlockState(), 3);
        return true;
    }
}
