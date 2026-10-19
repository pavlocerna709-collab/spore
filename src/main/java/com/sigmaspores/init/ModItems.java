package com.sigmaspores.init;

import com.sigmaspores.SigmaSpores;
import com.sigmaspores.item.SerumItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SigmaSpores.MODID);

    public static final RegistryObject<Item> INFECTED_MASS = ITEMS.register("infected_mass",
            () -> new BlockItem(ModBlocks.INFECTED_MASS.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Item> FUNGAL_CORE = ITEMS.register("fungal_core",
            () -> new BlockItem(ModBlocks.FUNGAL_CORE.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    public static final RegistryObject<Item> ANTISPORE_SERUM = ITEMS.register("antispore_serum",
            () -> new SerumItem(new Item.Properties().tab(ItemGroup.TAB_BREWING).stacksTo(16)));

    public static final RegistryObject<Item> SPORE_ZOMBIE_SPAWN_EGG = ITEMS.register("spore_zombie_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SPORE_ZOMBIE, 0x4A5D23, 0xD94FB0,
                    new Item.Properties().tab(ItemGroup.TAB_MISC)));
}
