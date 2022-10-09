package net.ersei.dml.modular_armor.mixin;

/*
 *
 *  Copyright (C) 2021 Nathan P. Bombana, IterationFunk
 *
 *  This file is part of Deep Mob Learning: Backported.
 *
 *  Deep Mob Learning: Backported is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Deep Mob Learning: Backported is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Deep Mob Learning: Backported.  If not, see <https://www.gnu.org/licenses/>.
 */

import net.ersei.dml.DeepMobLearningKt;
import net.ersei.dml.modular_armor.EntityStatusEffectsKt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ClientEntityMixin {

    @Shadow public World world;

    @Inject(at = @At("RETURN"), method = "isGlowing", cancellable = true)
    public void isGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (this.world.isClient && !cir.getReturnValue()) {
            if ((Object) this instanceof LivingEntity dis) {
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (player != null && dis != player && player.hasStatusEffect(EntityStatusEffectsKt.getSOUL_VISION_EFFECT())) {
                    if (player.distanceTo(dis) <= DeepMobLearningKt.getConfig().getGlitchArmor().getSoulVisionRange()) {
                        cir.setReturnValue(true);
                        cir.cancel();
                    }
                }
            }
        }
    }
}
