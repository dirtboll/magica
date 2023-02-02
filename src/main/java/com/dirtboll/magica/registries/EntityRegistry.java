package com.dirtboll.magica.registries;

import com.dirtboll.magica.Magica;
import com.dirtboll.magica.entities.CrystalSerpentProjectile;
import com.dirtboll.magica.entities.FireSparkProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Magica.MOD_ID);
    public static final RegistryObject<EntityType<FireSparkProjectile>> FIRE_SPARK = ENTITIES.register("fire_spark", () ->
        EntityType.Builder.of((EntityType.EntityFactory<FireSparkProjectile>) FireSparkProjectile::new, MobCategory.MISC)
            .sized(0.1f, 0.1f)
            .fireImmune()
            .setTrackingRange(1)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(60)
            .build("fire_spark")
    );


    public static void register() {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
