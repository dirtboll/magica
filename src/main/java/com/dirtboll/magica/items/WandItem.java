package com.dirtboll.magica.items;

import com.dirtboll.magica.Magica;
import com.dirtboll.magica.capabilities.wand.WandCapability;
import com.dirtboll.magica.registries.CapabilityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class WandItem extends Item {

    protected Random random = new Random();

    public WandItem(Item.Properties properties) {
        super(properties.tab(MagicaCreativeTab.instance));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @org.jetbrains.annotations.Nullable CompoundTag nbt) {
        var capProvider = super.initCapabilities(stack, nbt);
        if (capProvider != null) {
            var optCap = capProvider.getCapability(CapabilityRegistry.WAND_CAPABILITY);
            optCap.ifPresent(cap -> {
                    cap.setCoolDown(getCoolDown());
                }
            );
        }
        return capProvider;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack wand = player.getItemInHand(hand);
        WandCapability capability = getWandCapability(wand);
        if (capability != null && capability.doCast()) {
            cast(player, hand);
            capability.setCoolDown(getCoolDown());
            return InteractionResultHolder.pass(wand);
        } else {
            return InteractionResultHolder.fail(wand);
        }
    }


    abstract long getCoolDown();

    void cast(Player caster, InteractionHand hand) {
        caster.swing(hand);

        if (caster.level.isClientSide)
            return;

        ItemStack wand = caster.getItemInHand(hand);
        wand.hurtAndBreak(1, caster, (p_40665_) -> {
            p_40665_.broadcastBreakEvent(caster.getUsedItemHand());
        });
    };

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag baseTag = stack.getTag();

        CompoundTag combinedTag = new CompoundTag();
        if (baseTag != null) {
            combinedTag.put("base", baseTag);
        }
        WandCapability cap = getWandCapability(stack);
        if (cap != null) {
            combinedTag.put("capability", cap.serializeNBT());
        }
        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundTag baseTag = nbt.getCompound("base");
        CompoundTag capabilityTag = nbt.getCompound("capability");
        stack.setTag(baseTag);
        WandCapability cap = getWandCapability(stack);
        if (cap != null) {
            cap.deserializeNBT(capabilityTag);
        }
    }

    public static WandCapability getWandCapability(ItemStack stack) {
        var capabilityOpt = stack.getCapability(CapabilityRegistry.WAND_CAPABILITY).resolve();
        if (capabilityOpt.isEmpty()) {
            Magica.LOGGER.error("WandItem did not have the expected WAND_CAPABILITY");
            return null;
        }
        return capabilityOpt.get();
    }
}
