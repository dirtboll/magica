package com.dirtboll.magica.registries.capabilityattacher;

import com.dirtboll.magica.Magica;
import com.dirtboll.magica.capabilities.wand.WandCapability;
import com.dirtboll.magica.capabilities.wand.WandCapabilityImpl;
import com.dirtboll.magica.items.WandItem;
import com.dirtboll.magica.items.WandOfSparking;
import com.dirtboll.magica.registries.CapabilityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Magica.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WandCapabilityAttacher {
    private static class  WandCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(Magica.MOD_ID, "wand_capability");

        private final WandCapability backend = new WandCapabilityImpl();
        private final LazyOptional<WandCapability> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return CapabilityRegistry.WAND_CAPABILITY.orEmpty(cap, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    @SubscribeEvent
    public static void attachCapability(final AttachCapabilitiesEvent<ItemStack> event) {
        if (!(event.getObject().getItem() instanceof WandItem)) return;

        final WandCapabilityProvider provider = new WandCapabilityProvider();
        event.addCapability(WandCapabilityProvider.IDENTIFIER, provider);
    }

    private WandCapabilityAttacher() {}
}
