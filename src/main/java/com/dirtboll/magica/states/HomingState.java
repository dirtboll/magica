package com.dirtboll.magica.states;

import com.dirtboll.magica.utils.MathUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class HomingState extends State<HomingState> implements ITargetingState<HomingState> {

    private Entity subject;
    private Entity target;
    private int homingDistance;
    private int homingDistanceSqr;

    public int getHomingDistance() {
        return homingDistance;
    }

    public void setHomingDistance(int homingDistance) {
        this.homingDistance = homingDistance;
        this.homingDistanceSqr = homingDistance * homingDistance;
    }

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

    public static Function<HomingState, @Nullable IState<?>> homingProcessFactory(IState<?> nextState) {
        return (HomingState state) -> {
            var subject = state.getSubject();
            var target = state.getTarget();

            if (subject == null || target == null || !subject.isAlive() || !target.isAlive())
                return null;

            var pos2 = target.getBoundingBox().getCenter();
            var pos1 = subject.position();
            var dPos = new Vec3(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);

            if (dPos.lengthSqr() > state.homingDistanceSqr)
                return null;

            var vel = subject.getDeltaMovement();
//            var vA = vel.normalize();
//            var vB = dPos.normalize();
//            var up = vA.cross(vB);
//            var vRes = MathUtil.rodriguesRot(vel, up, 45);
            var vRes = vel.lerp(dPos, 0.2);

            subject.setDeltaMovement(vRes.x, vRes.y, vRes.z);
            return nextState;
        };
    }
}
