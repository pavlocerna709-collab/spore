package com.sigmaspores.event;

import com.sigmaspores.SigmaSpores;
import com.sigmaspores.entity.SporeZombieEntity;
import com.sigmaspores.init.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SigmaSpores.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void onAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SPORE_ZOMBIE.get(), SporeZombieEntity.createSporeAttributes().build());
    }
}
