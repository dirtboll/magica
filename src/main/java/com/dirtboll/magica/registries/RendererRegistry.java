package com.dirtboll.magica.registries;

import com.dirtboll.magica.Magica;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

//@Mod.EventBusSubscriber(modid = Magica.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RendererRegistry {

    @SubscribeEvent
    public static void register(final FMLClientSetupEvent event) {
        Magica.LOGGER.info("Registering renderers...");
        EntityRenderers.register(EntityRegistry.FIRE_SPARK.get(), ThrownItemRenderer::new);
    }
}
