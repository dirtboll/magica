package com.dirtboll.magica.capabilities.wand;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.monster.Zombie;

public class WandCapabilityImpl implements WandCapability {

    private boolean dirty = true;

    private void setDirty() {
        this.dirty = true;
    }

    public static String COOL_DOWN_TAG = "coolDown";
    public static String LAST_CAST_TAG = "lastCast";

    private long cooldown;
    private long lastCast;

    @Override
    public boolean canCast() {
        return System.currentTimeMillis() > lastCast + cooldown;
    }

    @Override
    public void setLastCast(long lastCast) {
        this.lastCast = lastCast;
        this.setDirty();
    }

    @Override
    public long getLastCast() {
        return this.lastCast;
    }

    @Override
    public void setCoolDown(long coolDown) {
        this.cooldown = coolDown;
        this.setDirty();
    }

    @Override
    public long getCoolDown() {
        return this.cooldown;
    }

    @Override
    public boolean doCast() {
        boolean casted = canCast();
        if (casted)
            this.setLastCast(System.currentTimeMillis());
        return casted;
    }

    private CompoundTag tag = new CompoundTag();

    @Override
    public CompoundTag serializeNBT() {
        if (!this.dirty)
            return tag;
        this.tag.putLong(COOL_DOWN_TAG, this.getCoolDown());
        this.tag.putLong(LAST_CAST_TAG, this.getLastCast());
        this.dirty = false;
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setCoolDown(nbt.getLong(COOL_DOWN_TAG));
        this.setLastCast(nbt.getLong(LAST_CAST_TAG));
    }
}
