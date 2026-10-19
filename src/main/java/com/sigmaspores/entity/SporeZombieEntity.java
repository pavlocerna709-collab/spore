package com.sigmaspores.entity;

import com.sigmaspores.block.InfectedMassBlock;
import com.sigmaspores.infection.Infection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SporeZombieEntity extends ZombieEntity {
    public SporeZombieEntity(EntityType<? extends ZombieEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createSporeAttributes() {
        return ZombieEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.27D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected boolean isSunSensitive() {
        return false; // грибы не горят на солнце
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity) {
            Infection.infect((LivingEntity) target);
        }
        return hit;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        // Зомби сам разносит грибницу по земле, по которой ходит
        if (!this.level.isClientSide && this.isAlive() && this.random.nextInt(400) == 0) {
            InfectedMassBlock.tryInfect((ServerWorld) this.level, this.blockPosition().below(), this.random);
        }
    }
}
