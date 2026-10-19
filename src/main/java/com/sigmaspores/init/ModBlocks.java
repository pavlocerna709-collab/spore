package com.sigmaspores.init;

import com.sigmaspores.SigmaSpores;
import com.sigmaspores.block.FungalCoreBlock;
import com.sigmaspores.block.InfectedMassBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SigmaSpores.MODID);

    public static final RegistryObject<Block> INFECTED_MASS = BLOCKS.register("infected_mass",
            () -> new InfectedMassBlock(AbstractBlock.Properties.of(Material.CLAY)
                    .strength(1.0F).sound(SoundType.WART_BLOCK).randomTicks(), 2));

    public static final RegistryObject<Block> FUNGAL_CORE = BLOCKS.register("fungal_core",
            () -> new FungalCoreBlock(AbstractBlock.Properties.of(Material.CLAY)
                    .strength(3.0F).sound(SoundType.WART_BLOCK).lightLevel(state -> 9).randomTicks(), 10));
}
