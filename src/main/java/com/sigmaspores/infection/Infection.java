package com.sigmaspores.infection;

import com.sigmaspores.block.InfectedMassBlock;
import com.sigmaspores.entity.SporeZombieEntity;
import com.sigmaspores.init.ModBlocks;
import com.sigmaspores.init.ModEffects;
import com.sigmaspores.init.ModEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public final class Infection {
    /** Сколько тиков длится инфекция до превращения (6000 = 5 минут). */
    public static final int TOTAL = 6000;
    private static final String FLAG = "sigmaTransformed";
    private static final String IMMUNE = "sigmaImmuneUntil";

    public static final DamageSource SPORE = new DamageSource("sigmaspores.spore") {{
        bypassArmor();
    }};

    private Infection() {}

    public static void infect(LivingEntity e) {
        if (e.level.isClientSide || !e.isAlive()) return;
        if (e instanceof SporeZombieEntity || e instanceof ArmorStandEntity || e.getMaxHealth() > 100) return;
        if (e instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) e;
            if (p.isCreative() || p.isSpectator()) return;
        }
        if (e.hasEffect(ModEffects.SPORE_INFECTION.get())) return;
        if (e.level.getGameTime() < e.getPersistentData().getLong(IMMUNE)) return;
        e.addEffect(new EffectInstance(ModEffects.SPORE_INFECTION.get(), TOTAL, 0, false, true, true));
        if (e instanceof PlayerEntity) {
            ((PlayerEntity) e).displayClientMessage(new TranslationTextComponent("message.sigmaspores.infected"), false);
        }
    }

    public static void cure(LivingEntity e) {
        e.removeEffect(ModEffects.SPORE_INFECTION.get());
        e.getPersistentData().remove(FLAG);
        e.getPersistentData().putLong(IMMUNE, e.level.getGameTime() + 6000L); // 5 минут иммунитета
    }

    public static int stage(int duration) {
        double p = 1.0 - (double) duration / TOTAL;
        return p < 0.25 ? 0 : p < 0.5 ? 1 : p < 0.75 ? 2 : 3;
    }

    /** Вызывается раз в тик на сервере для заражённой сущности. */
    public static void tick(LivingEntity e, EffectInstance inst) {
        if (e instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) e;
            if (p.isCreative() || p.isSpectator()) {
                e.removeEffect(ModEffects.SPORE_INFECTION.get());
                return;
            }
        }
        int dur = inst.getDuration();
        if (dur <= 20) {
            transformAndKill(e);
            return;
        }
        if (e.tickCount % 20 != 0) return;

        int stage = stage(dur);
        ServerWorld w = (ServerWorld) e.level;
        Random r = e.getRandom();

        w.sendParticles(stage >= 2 ? ParticleTypes.CRIMSON_SPORE : ParticleTypes.WARPED_SPORE,
                e.getX(), e.getY(0.5D), e.getZ(), 4 + stage * 3,
                e.getBbWidth() / 2.0, e.getBbHeight() / 3.0, e.getBbWidth() / 2.0, 0.02D);

        if (e instanceof PlayerEntity && stage > 0 && stage != stage(dur + 20)) {
            ((PlayerEntity) e).displayClientMessage(new TranslationTextComponent("message.sigmaspores.stage" + stage), true);
        }

        if (stage >= 1 && r.nextInt(3) == 0) {
            e.addEffect(new EffectInstance(Effects.HUNGER, 80, 0, false, false));
        }
        if (stage >= 2) {
            e.addEffect(new EffectInstance(Effects.WEAKNESS, 60, 0, false, false));
            e.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, stage >= 3 ? 1 : 0, false, false));
            // заразный: передаёт споры всем рядом
            for (LivingEntity t : w.getEntitiesOfClass(LivingEntity.class, e.getBoundingBox().inflate(2.5D), x -> x != e)) {
                if (r.nextInt(5) == 0) infect(t);
            }
        }
        if (stage >= 3) {
            if (e instanceof PlayerEntity) e.addEffect(new EffectInstance(Effects.CONFUSION, 100, 0, false, false));
            if (r.nextInt(4) == 0) e.addEffect(new EffectInstance(Effects.BLINDNESS, 40, 0, false, false));
            if (e.getHealth() > 2.0F) e.hurt(SPORE, 1.0F);
        }
    }

    /** Превращает сущность в грибного зомби (один раз). */
    public static void transform(LivingEntity e) {
        if (!(e.level instanceof ServerWorld)) return;
        if (e.getPersistentData().getBoolean(FLAG)) return;
        e.getPersistentData().putBoolean(FLAG, true);
        ServerWorld w = (ServerWorld) e.level;

        SporeZombieEntity z = ModEntities.SPORE_ZOMBIE.get().create(w);
        if (z != null) {
            z.moveTo(e.getX(), e.getY(), e.getZ(), e.yRot, e.xRot);
            z.finalizeSpawn(w, w.getCurrentDifficultyAt(e.blockPosition()), SpawnReason.CONVERSION, null, null);
            z.setBaby(e.isBaby());
            if (e instanceof PlayerEntity) {
                z.setCustomName(e.getName());
                z.setCustomNameVisible(true);
            } else if (e.hasCustomName()) {
                z.setCustomName(e.getCustomName());
            }
            z.setPersistenceRequired();
            w.addFreshEntity(z);
        }

        BlockPos pos = e.blockPosition();
        InfectedMassBlock.tryInfect(w, pos.below(), w.random);
        if (w.random.nextInt(4) == 0 && w.getBlockState(pos).getMaterial().isReplaceable()) {
            w.setBlock(pos, ModBlocks.FUNGAL_CORE.get().defaultBlockState(), 3);
        }
        w.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1.0F, 0.7F);
    }

    public static void transformAndKill(LivingEntity e) {
        transform(e);
        e.hurt(SPORE, Float.MAX_VALUE);
        if (e.isAlive()) { // тотем бессмертия и т.п.
            e.getPersistentData().remove(FLAG);
            e.removeEffect(ModEffects.SPORE_INFECTION.get());
        }
    }
}
