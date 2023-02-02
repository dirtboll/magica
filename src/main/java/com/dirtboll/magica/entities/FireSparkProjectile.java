package com.dirtboll.magica.entities;

import com.dirtboll.magica.registries.EntityRegistry;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class FireSparkProjectile extends ThrowableItemProjectile {

    // TODO: Use config
//    public static int DEFAULT_LIFETIME = 5;
    public static float DEFAULT_HIT_DAMAGE = 4f;
    public static int DEFAULT_IGNITE_DURATION_MIN = 2;
    public static int DEFAULT_IGNITE_DURATION_RANGE = 5;

//    private int tickLeft = DEFAULT_LIFETIME;

    public FireSparkProjectile(EntityType<? extends FireSparkProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FireSparkProjectile(Level level, Player player) {
        super(EntityRegistry.FIRE_SPARK.get(), player, level);
    }

    private ParticleOptions getParticle() {
        return ParticleTypes.FLAME;
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }


    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity == this.getOwner())
            return;
        entity.hurt(new IndirectEntityDamageSource("magic", this,
                getOwner()).setIsFire().setMagic().setProjectile(), DEFAULT_HIT_DAMAGE);
        entity.setSecondsOnFire(getRandomIgniteDuration());
        kill();
    }

    @Override
    public void tick() {
        super.tick();
//        if (tickLeft < 0) {
//            kill();
//        }
//        tickLeft -= 1;
        if (isInWater() || isInPowderSnow)
            kill();

        if (this.level.isClientSide) {
            var particle = Minecraft.getInstance().particleEngine.createParticle(this.getParticle(), this.getX(), this.getY(), this.getZ(), this.random.nextDouble() * 0.01, this.random.nextDouble() * 0.01, this.random.nextDouble() * 0.01);
            particle.setLifetime(this.random.nextInt(3,5));
        }

    }

    public int getRandomIgniteDuration() {
        return this.random.nextInt(DEFAULT_IGNITE_DURATION_MIN, DEFAULT_IGNITE_DURATION_MIN + DEFAULT_IGNITE_DURATION_RANGE);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR;
    }


}
