package com.sigmaspores.init;

import net.minecraft.world.GameRules;

public class ModGameRules {
    public static GameRules.RuleKey<GameRules.BooleanValue> SPREAD;

    public static void init() {
        SPREAD = GameRules.register("sigmaSporeSpread", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));
    }
}
