package net.ersei.dml.mixin;

/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Backported.
 *
 * Deep Mob Learning: Backported is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * Deep Mob Learning: Backported is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Backported.  If not, see <https://www.gnu.org/licenses/>.
 */

import net.ersei.dml.accessor.ILivingEntityReiStateAccessor;
import net.ersei.dml.entity.SystemGlitchEntity;
import net.ersei.dml.event.VanillaEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements ILivingEntityReiStateAccessor {

    boolean dmlRefIsInREIScreen = false;

    @Inject(at = @At("HEAD"), method = "getMaxHealth", cancellable = true)
    public void getMaxHealth(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof SystemGlitchEntity dis) {
            if (dis.getTier() != null) {
                float health = dis.getTier().getSystemGlitchMaxHealth();
                cir.setReturnValue(health);
                cir.cancel();
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "eatFood")
    public void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isFood()) {
            VanillaEvents.INSTANCE.getLivingEntityEatEvent()
                    .invoker()
                    .invoke((LivingEntity) (Object) this, stack);
        }
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"), method = "applyDamage")
    private float applyDamage(DamageSource source, float amount) {
        LivingEntity dis = (LivingEntity) (Object) this;
        return VanillaEvents.INSTANCE.getLivingEntityDamageEvent()
                .invoker()
                .invoke(new VanillaEvents.LivingEntityDamageContext(dis, source, amount))
                .getDamage();
    }

    @Override
    public boolean isDmlRefIsInReiScreen() {
        return dmlRefIsInREIScreen;
    }

    @Override
    public void setDmlRefInReiScreen(boolean flag) {
        dmlRefIsInREIScreen = flag;
    }


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"), method = "onEquipStack", cancellable = true)
	public void onEquip(ItemStack stack, CallbackInfo ci) {
        if (isDmlRefIsInReiScreen()) {
            ci.cancel();
        }
    }

}
