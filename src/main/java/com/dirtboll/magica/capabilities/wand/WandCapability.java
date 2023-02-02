package com.dirtboll.magica.capabilities.wand;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface WandCapability extends INBTSerializable<CompoundTag> {
    boolean canCast();

    void setLastCast(long lastCast);
    long getLastCast();

    void setCoolDown(long coolDown);
    long getCoolDown();

    boolean doCast();
}
