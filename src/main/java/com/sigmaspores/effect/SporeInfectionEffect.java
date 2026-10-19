package com.sigmaspores.effect;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

/** Маркер-эффект. Вся логика стадий живёт в Infection / InfectionEvents. */
public class SporeInfectionEffect extends Effect {
    public SporeInfectionEffect() {
        super(EffectType.HARMFUL, 0x8FBF3A);
    }
}
