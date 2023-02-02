package com.dirtboll.magica.items;

import com.dirtboll.magica.entities.CrystalSerpentProjectile;
import com.dirtboll.magica.entities.FireSparkProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class CrystalSerpent extends WandItem {

    public static int MAX_DAMAGE = 120;
    public static long COOL_DOWN_MIN = 500;
    public static long COOL_DOWN_RANGE = 200;

    public CrystalSerpent() {
        super(new Item.Properties()
                .stacksTo(1)
                .durability(MAX_DAMAGE)
                .rarity(Rarity.EPIC)
        );
    }

    @Override
    long getCoolDown() {
        return COOL_DOWN_MIN + (long)(random.nextDouble() * (double) COOL_DOWN_RANGE);
    }

    @Override
    void cast(Player caster, InteractionHand hand) {
        super.cast(caster, hand);

        var view = caster.getViewVector(1.0F);
        var projectile = new CrystalSerpentProjectile(caster.level, caster);
        projectile.shoot(view.x, view.y, view.z, 1.8f, 5f);
        caster.level.addFreshEntity(projectile);

        caster.level.playSound(null, caster, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, caster.getVoicePitch());

    }
}
