package com.dirtboll.magica.states;

import lombok.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Getter
@Setter
@RequiredArgsConstructor
public class HomingState extends State<HomingState> implements ITargetingState<HomingState> {

    @NonNull private Entity subject;
    private Entity target;
    private int homingDistance = 10;
    private float tickFactor = 0.0001F;
    private float tickFactorFactor = 2F;


    @Setter(AccessLevel.PRIVATE) private float currentTickFactor = 0F;
    @Setter(AccessLevel.PRIVATE) private int homingDistanceSqr = 100;
    @Setter(AccessLevel.PRIVATE) private float tickElapsed = 0F;

    public void setHomingDistance(int homingDistance) {
        this.homingDistance = homingDistance;
        this.homingDistanceSqr = homingDistance * homingDistance;
    }

    @Override
    public void onChangeState(IState<?> from, IState<?> to) {
        super.onChangeState(from, to);
        if (to == this) {
            this.currentTickFactor = this.tickFactor;
            this.tickElapsed = this.currentTickFactor;
        }
    }

    public Function<HomingState, @Nullable IState<?>> homingProcessFactory() {
        return (HomingState state) -> {
            var subject = state.getSubject();
            var target = state.getTarget();

            if (target == null || !target.isAlive() || !subject.isAlive())
                return null;

            var pos2 = target.getBoundingBox().getCenter();
            var pos1 = subject.getBoundingBox().getCenter();
            var dPos = new Vec3(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);

//            if (state.homingDistanceSqr > 0 && dPos.lengthSqr() > state.homingDistanceSqr) {
//                Magica.LOGGER.info("Too far! {} {} {} {}", dPos.lengthSqr(), state.homingDistanceSqr, target, subject);
//                return null;
//            }


            var vel = subject.getDeltaMovement();
            var vRes = vel.lerp(dPos, state.tickElapsed);
            state.currentTickFactor *= state.tickFactorFactor;
            state.tickElapsed += state.currentTickFactor;

            subject.setDeltaMovement(vRes.x, vRes.y, vRes.z);
            return state;
        };
    }

    public Function<HomingState, @Nullable IState<?>> fallbackProcessFactory(IState<?> nextState) {
        return (state) -> {
            if (!state.getSubject().isAlive())
                return null;
            return nextState;
        };
    }
}
