package net.ersei.dml.mixin;

/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Backported.
 *
 * Deep Mob Learning: Backported is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deep Mob Learning: Backported is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Backported.  If not, see <https://www.gnu.org/licenses/>.
 */

import net.ersei.dml.event.VanillaEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;applyEnchantmentsToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"), method = "applyDamage")
    private float applyDamage(DamageSource source, float amount) {
        PlayerEntity dis = (PlayerEntity) (Object) this;
        return VanillaEvents.INSTANCE.getLivingEntityDamageEvent()
            .invoker()
            .invoke(new VanillaEvents.LivingEntityDamageContext(dis, source, amount))
            .getDamage();
    }


    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        VanillaEvents.INSTANCE.getPlayerEntityTickEvent()
            .invoker()
            .invoke((PlayerEntity) (Object) this);
    }

}
