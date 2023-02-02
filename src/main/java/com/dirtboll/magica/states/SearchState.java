package com.dirtboll.magica.states;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class SearchState extends State<SearchState> {

    private Entity subject;
    private int searchRadius;

    private Predicate<Entity> filterPredicate;

    public int getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(int searchRadius) {
        this.searchRadius = searchRadius;
    }

    public Entity getSubject() {
        return subject;
    }

    public void setSubject(Entity subject) {
        this.subject = subject;
    }

    public static Function<SearchState, @Nullable IState<?>> searchProcessFactory(ITargetingState<?> nextState) {
        return (SearchState state) -> {
            var subject = state.getSubject();

            if (subject == null || !subject.isAlive())
                return null;

            var bound = subject.getBoundingBox();

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

            if (t == null)
                return null;

            nextState.setTarget(t);
            nextState.setSubject(subject);

            return nextState;
        };
    }

    public Predicate<Entity> getFilterPredicate() {
        return filterPredicate;
    }

    public void setFilterPredicate(Predicate<Entity> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }
}
