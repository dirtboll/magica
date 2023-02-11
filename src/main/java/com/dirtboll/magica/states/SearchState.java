package com.dirtboll.magica.states;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
@Setter
public class SearchState extends State<SearchState> {

    @NonNull private Entity subject;
    private int searchRadius = 10;
    @Setter(AccessLevel.PRIVATE) private Predicate<Entity> filterPredicate = EntitySelector.NO_CREATIVE_OR_SPECTATOR
            .and(e -> e instanceof LivingEntity);
    @Setter(AccessLevel.PRIVATE) private Entity foundTarget;

    public SearchState(@NotNull Entity subject) {
        this.subject = subject;
    }

    public void addFilterPredicate(Predicate<Entity> pred) {
        this.filterPredicate = this.filterPredicate.and(pred);
    }

    private AABB getSearchArea(Entity subject) {
        return AABB.unitCubeFromLowerCorner(subject.getEyePosition()).inflate(this.searchRadius);
    }

    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }

    private boolean isInSight(Entity eA, Entity eB) {
        if (eB.level != eA.level) {
            return false;
        } else {
            Vec3 aVec = new Vec3(eA.getX(), eA.getEyeY(), eA.getZ());
            Vec3 bVec = new Vec3(eB.getX(), eB.getY(), eB.getZ());
            if (bVec.distanceTo(aVec) > 128.0D) {
                return false;
            } else {
                var hitRes = eA.level.clip(new ClipContext(aVec, bVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, eA));
                return hitRes.getType() == HitResult.Type.MISS;
            }
        }
    }

    public Function<SearchState, @Nullable IState<?>> searchProcessFactory(IState<?> nextState) {
        return (SearchState state) -> {
            state.setFoundTarget(null);
            var subject = state.getSubject();

            if (!subject.isAlive())
                return null;

            List<Entity> entities = subject.level.getEntities(
                    subject,
                    state.getSearchArea(subject),
                    state.getFilterPredicate()
            );

            double d0 = Double.MAX_VALUE;
            Entity t = null;

            for(Entity t1 : entities) {
                double d1 = t1.distanceTo(subject);
                boolean inSight = isInSight(t1, subject);
                if (d1 < d0 && inSight) {
                    d0 = d1;
                    t = t1;
                }
            }

            if (t != null) {
                state.setFoundTarget(t);
                return null;
            }

            return nextState;
        };
    }

    public Function<SearchState, @Nullable IState<?>> fallbackProcessFactory(ITargetingState<?> nextState) {
        return (SearchState state) -> {
            if (state.getFoundTarget() == null)
                return null;
            nextState.setTarget(state.getFoundTarget());
            nextState.setSubject(state.getSubject());
            return nextState;
        };
    }
}
