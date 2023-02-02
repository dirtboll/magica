package com.dirtboll.magica.capabilities.state;

import com.dirtboll.magica.states.State;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;

public interface StateCapability extends INBTSerializable<CompoundTag> {
    State getState();
    void setState(State state);
    void process(Entity entity);
}
