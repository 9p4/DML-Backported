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

package net.ersei.dml.modular_armor.effects

import com.mojang.datafixers.util.Pair
import net.ersei.dml.config
import net.ersei.dml.enums.DataModelTier
import net.ersei.dml.enums.EntityCategory
import net.ersei.dml.identifier
import net.ersei.dml.modular_armor.core.*
import net.ersei.dml.modular_armor.data.ModularArmorData
import net.ersei.dml.utils.firstInstanceOrNull
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ActionResult
import kotlin.random.Random

class RotResistanceEffect : ModularEffect<ModularEffectTriggerPayload>(
    identifier("rot_resistance"),
    EntityCategory.ZOMBIE,
    config.glitchArmor.costs::rotResistance
) {

    companion object {
        private val INSTANCE = ModularEffectRegistry.INSTANCE.all.firstInstanceOrNull<RotResistanceEffect>()

        fun attemptToCancelHunger(player: LivingEntity, stack: ItemStack, effects: List<Pair<StatusEffectInstance, Float>>): List<Pair<StatusEffectInstance, Float>> {
            INSTANCE?.run {
                if (player is PlayerEntity && !player.world.isClient && stack.item == Items.ROTTEN_FLESH) {
                    ModularEffectContext.from(player)
                        .run(EffectStackOption.PRIORITIZE_GREATER.apply)
                        .firstOrNull { context ->
                            attemptToApply(context, ModularEffectTriggerPayload.EMPTY) == ActionResult.SUCCESS
                        }?.let { context ->
                            if (context.tier.ordinal > 0 || Random.nextFloat() <= .5) {
                                return effects.filter { pair ->
                                    pair.first.effectType != StatusEffects.HUNGER
                                }
                            }
                        }
                }
            }


            return effects
        }
    }

    override fun registerEvents() {
    }

    override fun acceptTier(tier: DataModelTier) = true

    override fun createEntityAttributeModifier(armor: ModularArmorData): EntityAttributeModifier {
        val value = if (armor.tier() == DataModelTier.FAULTY) 0.5 else 1.0
        return EntityAttributeModifier(id.toString(), value, EntityAttributeModifier.Operation.MULTIPLY_BASE)
    }

}
