package com.dirtboll.magica.items;

import com.dirtboll.magica.Magica;
import com.dirtboll.magica.registries.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class MagicaCreativeTab extends CreativeModeTab {
    public static final MagicaCreativeTab instance = new MagicaCreativeTab(CreativeModeTab.TABS.length, Magica.MOD_ID);

    private MagicaCreativeTab(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemRegistry.WAND_OF_SPARKING.get());
    }
}
