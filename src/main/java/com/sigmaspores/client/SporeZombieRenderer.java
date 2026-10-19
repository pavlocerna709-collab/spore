package com.sigmaspores.client;

import com.sigmaspores.SigmaSpores;
import com.sigmaspores.entity.SporeZombieEntity;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;

public class SporeZombieRenderer extends AbstractZombieRenderer<SporeZombieEntity, ZombieModel<SporeZombieEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SigmaSpores.MODID, "textures/entity/spore_zombie.png");

    public SporeZombieRenderer(EntityRendererManager manager) {
        super(manager, new ZombieModel<>(0.0F, false), new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieEntity entity) {
        return TEXTURE;
    }
}
