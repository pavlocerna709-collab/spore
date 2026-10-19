package com.sigmaspores.init;

import com.sigmaspores.SigmaSpores;
import com.sigmaspores.effect.SporeInfectionEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, SigmaSpores.MODID);

    public static final RegistryObject<Effect> SPORE_INFECTION = EFFECTS.register("spore_infection", SporeInfectionEffect::new);
}
