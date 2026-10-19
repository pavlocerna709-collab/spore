package com.sigmaspores.event;

import com.sigmaspores.block.InfectedMassBlock;
import com.sigmaspores.entity.SporeZombieEntity;
import com.sigmaspores.infection.Infection;
import com.sigmaspores.init.ModEffects;
import com.sigmaspores.init.ModEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InfectionEvents {

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity e = event.getEntityLiving();
        if (e.level.isClientSide) return;

        // Стоишь на заражённом блоке - рискуешь подхватить споры
        if (e.tickCount % 20 == 0 && !(e instanceof SporeZombieEntity)) {
            if (e.level.getBlockState(e.blockPosition().below()).getBlock() instanceof InfectedMassBlock
                    && e.getRandom().nextInt(4) == 0) {
                Infection.infect(e);
            }
        }

        EffectInstance inst = e.getEffect(ModEffects.SPORE_INFECTION.get());
        if (inst != null) Infection.tick(e, inst);
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        LivingEntity e = event.getEntityLiving();
        if (!e.level.isClientSide && e.hasEffect(ModEffects.SPORE_INFECTION.get())) {
            Infection.transform(e);
        }
    }

    @SubscribeEvent
    public void onBiomeLoad(BiomeLoadingEvent event) {
        Biome.Category c = event.getCategory();
        if (c == Biome.Category.NETHER || c == Biome.Category.THEEND) return;
        event.getSpawns().addSpawn(EntityClassification.MONSTER,
                new MobSpawnInfo.Spawners(ModEntities.SPORE_ZOMBIE.get(), 60, 3, 6));
    }
}
