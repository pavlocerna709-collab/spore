package com.sigmaspores.block;

import com.sigmaspores.entity.SporeZombieEntity;
import com.sigmaspores.init.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/** Ядро грибницы: светится, быстро разрастается и рожает грибных зомби. */
public class FungalCoreBlock extends InfectedMassBlock {
    public FungalCoreBlock(Properties props, int attempts) {
        super(props, attempts);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        super.randomTick(state, world, pos, rand);
        if (rand.nextInt(10) == 0 && world.isEmptyBlock(pos.above())) {
            int nearby = world.getEntitiesOfClass(SporeZombieEntity.class, new AxisAlignedBB(pos).inflate(12)).size();
            if (nearby < 3) {
                ModEntities.SPORE_ZOMBIE.get().spawn(world, null, null, pos.above(), SpawnReason.SPAWNER, true, false);
            }
        }
    }
}
