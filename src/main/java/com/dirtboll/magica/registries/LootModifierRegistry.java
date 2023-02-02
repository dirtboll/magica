package com.dirtboll.magica.registries;

import com.dirtboll.magica.Magica;
import com.dirtboll.magica.loots.LootInjectionModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootModifierRegistry {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLMS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Magica.MOD_ID);
    public static final RegistryObject<GlobalLootModifierSerializer<LootInjectionModifier>> LOOT_INJECTION = GLMS.register("loot_injection", LootInjectionModifier.Serializer::new);

    public static void register() {
        GLMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
