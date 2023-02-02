package com.dirtboll.magica.items;

import com.dirtboll.magica.Magica;
import com.dirtboll.magica.entities.FireSparkProjectile;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

public class WandOfSparking extends WandItem {
    public static int MAX_DAMAGE = 60;
    public static long COOL_DOWN_MIN = 500;
    public static long COOL_DOWN_RANGE = 200;

    public WandOfSparking() {
        super(new Item.Properties()
                .stacksTo(1)
                .durability(MAX_DAMAGE)
                .rarity(Rarity.RARE)
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
        for (int i = 0; i < 4; i++) {
            FireSparkProjectile fireSpark = new FireSparkProjectile(caster.level, caster);
            fireSpark.shoot(view.x, view.y+0.15, view.z, 1f, 5f);
            caster.level.addFreshEntity(fireSpark);
        }

        caster.level.playSound(null, caster, SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, caster.getVoicePitch());

    }
}
