package com.sigmaspores;

import com.sigmaspores.entity.SporeZombieEntity;
import com.sigmaspores.event.InfectionEvents;
import com.sigmaspores.init.*;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SigmaSpores.MODID)
public class SigmaSpores {
    public static final String MODID = "sigmaspores";

    public SigmaSpores() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModEffects.EFFECTS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModGameRules.init();
        bus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(new InfectionEvents());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> EntitySpawnPlacementRegistry.register(
                ModEntities.SPORE_ZOMBIE.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                MonsterEntity::checkMonsterSpawnRules));
    }
}
