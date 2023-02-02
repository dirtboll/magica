package com.dirtboll.magica.entities;

import com.dirtboll.magica.registries.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class CrystalSerpentProjectile extends ThrowableItemProjectile {
    public CrystalSerpentProjectile(EntityType<? extends CrystalSerpentProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public CrystalSerpentProjectile(Level level, Player player) {
        super(EntityRegistry.FIRE_SPARK.get(), player, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.NETHER_STAR;
    }

}
