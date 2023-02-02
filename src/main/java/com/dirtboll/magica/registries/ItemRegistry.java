package com.dirtboll.magica.registries;

import com.dirtboll.magica.Magica;
import com.dirtboll.magica.items.CrystalSerpent;
import com.dirtboll.magica.items.WandOfSparking;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Magica.MOD_ID);
    public static final RegistryObject<Item> WAND_OF_SPARKING = ITEMS.register("wand_of_sparking", WandOfSparking::new);
    public static final RegistryObject<Item> CRYSTAL_SERPENT = ITEMS.register("crystal_serpent", CrystalSerpent::new);

    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
