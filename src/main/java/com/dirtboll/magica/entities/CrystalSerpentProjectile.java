package com.dirtboll.magica.entities;

import com.dirtboll.magica.registries.EntityRegistry;
import com.dirtboll.magica.states.CoolDownState;
import com.dirtboll.magica.states.HomingState;
import com.dirtboll.magica.states.IState;
import com.dirtboll.magica.states.SearchState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class CrystalSerpentProjectile extends ThrowableItemProjectile {
    // TODO: Use config
    public static int DEFAULT_LIFETIME = 100;
    public static float DEFAULT_HIT_DAMAGE = 8f;
    private int tickLeft = DEFAULT_LIFETIME;

    private int generation = 0;

    private IState<?> aiState;

    public CrystalSerpentProjectile(EntityType<? extends CrystalSerpentProjectile> entityType, Level level) {
        super(entityType, level);
        this.initState();
    }

    public CrystalSerpentProjectile(Level level, Player player) {
        super(EntityRegistry.CRYSTAL_SERPENT.get(), player, level);
        this.initState();
    }

    private void initState() {
        var startCoolDownState = new CoolDownState((long) (100 >> ((this.generation+1) * 2)));

        var searchCoolDownState = new CoolDownState(200L);

        var searchState = new SearchState(this);
        searchState.addFilterPredicate((e) -> e != this.getOwner());
        searchState.setSearchRadius(5);

        var homingState = new HomingState(this);
        homingState.setHomingDistance(searchState.getSearchRadius());

        startCoolDownState.addProcess(startCoolDownState.coolDownProcessFactory());
        startCoolDownState.addProcess(startCoolDownState.fallbackProcessFactory(searchState));

        searchCoolDownState.addProcess(searchCoolDownState.coolDownProcessFactory());
        searchCoolDownState.addProcess(searchCoolDownState.fallbackProcessFactory(searchState));

        searchState.addProcess(searchState.searchProcessFactory(searchCoolDownState));
        searchState.addProcess(searchState.fallbackProcessFactory(homingState));

        homingState.addProcess(homingState.homingProcessFactory());
        homingState.addProcess(homingState.fallbackProcessFactory(searchState));

        this.aiState = startCoolDownState;
    }

    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity == this.getOwner())
            return;
        entity.hurt(new IndirectEntityDamageSource("magic", this,
                getOwner()).setMagic().setProjectile().bypassInvul(), DEFAULT_HIT_DAMAGE);

        var vel = Direction.UP.getNormal();
        if (this.generation <= 0) {
            for (int i = 0; i < 4; i++) {
                var projectile = new CrystalSerpentProjectile(this.level, (Player) getOwner());
                projectile.generation += 1;
                projectile.setPos(entityHitResult.getLocation());
                projectile.shoot(vel.getX(), vel.getY(), vel.getZ(), 1f, 30f);
                this.level.addFreshEntity(projectile);
            }
        }
        kill();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        var vel = blockHitResult.getDirection().getNormal();
        if (this.generation <= 0) {
            for (int i = 0; i < 4; i++) {
                var projectile = new CrystalSerpentProjectile(this.level, (Player) getOwner());
                projectile.generation += 1;
                projectile.setPos(blockHitResult.getLocation());
                projectile.shoot( vel.getX(), vel.getY(), vel.getZ(), 1f, 30f);
                this.level.addFreshEntity(projectile);
            }
        }
        kill();
    }

    @Override
    protected void onHit(HitResult hitRes) {
        super.onHit(hitRes);

    }

    @Override
    public void tick() {
        super.tick();
        if (tickLeft < 0) {
            kill();
        }
        tickLeft -= 1;

        if (this.level.isClientSide) {
            var particle = Minecraft.getInstance().particleEngine.createParticle(this.getParticle(), this.getX(), this.getY(), this.getZ(), this.random.nextDouble() * 0.01, this.random.nextDouble() * 0.01, this.random.nextDouble() * 0.01);
            particle.setLifetime(this.random.nextInt(3,5));
        } else {
            this.aiState = this.aiState.process();
        }

    }

    private ParticleOptions getParticle() {
        return ParticleTypes.SPIT;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.NETHER_STAR;
    }

    @Override
    public boolean isNoGravity() {
        return super.isNoGravity();
    }
}
