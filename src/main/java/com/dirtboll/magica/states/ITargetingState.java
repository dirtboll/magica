package com.dirtboll.magica.states;

import net.minecraft.world.entity.Entity;

public interface ITargetingState<T extends IState<T>> extends IState<T> {
    Entity getSubject();
    void setSubject(Entity subject);
    Entity getTarget();
    void setTarget(Entity target);
}
