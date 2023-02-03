package com.dirtboll.magica.states;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Data
public class SearchState extends State<SearchState> {

    @NonNull private Entity subject;
    private int searchRadius = 10;
    private Predicate<Entity> filterPredicate = e -> e != subject;
    @Setter(AccessLevel.PRIVATE) private Entity foundTarget;

    public static Function<SearchState, @Nullable IState<?>> searchProcessFactory() {
        return (SearchState state) -> {
            state.setFoundTarget(null);
            var subject = state.getSubject();

            if (!subject.isAlive())
                return null;

            List<LivingEntity> entities = subject.level.getEntitiesOfClass(LivingEntity.class, subject.getBoundingBox().inflate(state.searchRadius), state.filterPredicate == null ? (e) -> e != subject : state.filterPredicate);

            double d0 = -1.0D;
            LivingEntity t = null;
            var x = subject.getX();
            var y = subject.getY();
            var z = subject.getZ();

            for(LivingEntity t1 : entities) {
                double d1 = t1.distanceToSqr(x, y, z);
                if (d0 == -1.0D || d1 < d0) {
                    d0 = d1;
                    t = t1;
                }
            }

            if (t != null) {
                state.setFoundTarget(t);
                return null;
            }

            return state;
        };
    }

    public static Function<SearchState, @Nullable IState<?>> fallbackProcessFactory(ITargetingState<?> nextState) {
        return (SearchState state) -> {
            if (state.getFoundTarget() == null)
                return null;
            nextState.setTarget(state.getFoundTarget());
            nextState.setSubject(state.getSubject());
            return nextState;
        };
    }
}
