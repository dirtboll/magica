package com.dirtboll.magica.capabilities.state;

import com.dirtboll.magica.states.State;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class StateCapabilityImpl implements StateCapability {
    @Override
    public State getState() {
        return null;
    }

    @Override
    public void setState(State state) {

    }

    @Override
    public void process(Entity entity) {

    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
