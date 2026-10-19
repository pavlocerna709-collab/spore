package com.sigmaspores.init;

import com.sigmaspores.SigmaSpores;
import com.sigmaspores.entity.SporeZombieEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SigmaSpores.MODID);

    public static final RegistryObject<EntityType<SporeZombieEntity>> SPORE_ZOMBIE = ENTITIES.register("spore_zombie",
            () -> EntityType.Builder.of(SporeZombieEntity::new, EntityClassification.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(SigmaSpores.MODID, "spore_zombie").toString()));
}
