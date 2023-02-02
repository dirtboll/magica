package com.dirtboll.magica.states;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class HomingState extends State<HomingState> implements ITargetingState<HomingState> {

    private Entity subject;
    private Entity target;

    public Entity getSubject() {
        return subject;
    }

    public void setSubject(Entity subject) {
        this.subject = subject;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }


    public static Function<HomingState, @Nullable IState<?>> lerpProcessFactory() {
        return (HomingState state) -> {
            var subject = state.getSubject();
            var target = state.getTarget();

            if (subject == null || target == null || !subject.isAlive() || !target.isAlive())
                return null;

            var pos = target.position();
            subject.lerpMotion(pos.x, pos.y, pos.z);
            return state;
        };
    }

    public static Function<HomingState, @Nullable IState<?>> fallbackProcessFactory(SearchState searchState) {
        return (HomingState state) -> {
            var subject = state.getSubject();
            if (subject == null || !subject.isAlive())
                return null;

            searchState.setSubject(subject);
            return searchState;
        };
    }
}
