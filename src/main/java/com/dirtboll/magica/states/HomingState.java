package com.dirtboll.magica.states;

import com.dirtboll.magica.utils.MathUtil;
import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

@Data
public class HomingState extends State<HomingState> implements ITargetingState<HomingState> {

    @NonNull private Entity subject;
    private Entity target;
    private int homingDistance = 10;
    private int homingDistanceSqr = 100;
    private float tickFactor = 0.05F;

    @Setter(AccessLevel.PRIVATE) private float tickElapsed = 0F;

    @Override
    public void onChangeState(IState<?> from, IState<?> to) {
        super.onChangeState(from, to);
        if (to == this)
            this.tickElapsed = 0;
    }

    public static Function<HomingState, @Nullable IState<?>> homingProcessFactory() {
        return (HomingState state) -> {
            var subject = state.getSubject();
            var target = state.getTarget();

            if (target == null || !target.isAlive() || !subject.isAlive())
                return null;

            var pos2 = target.getBoundingBox().getCenter();
            var pos1 = subject.position();
            var dPos = new Vec3(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);

            if (state.homingDistanceSqr > 0 && dPos.lengthSqr() > state.homingDistanceSqr)
                return null;

            var vel = subject.getDeltaMovement();
            var vRes = vel.lerp(dPos, state.tickElapsed);

            subject.setDeltaMovement(vRes.x, vRes.y, vRes.z);
            state.tickElapsed += state.tickFactor;
            return state;
        };
    }

    public static Function<HomingState, @Nullable IState<?>> fallbackProcessFactory(IState<?> nextState) {
        return (state) -> {
            if (!state.getSubject().isAlive())
                return null;
            return nextState;
        };
    }
}
