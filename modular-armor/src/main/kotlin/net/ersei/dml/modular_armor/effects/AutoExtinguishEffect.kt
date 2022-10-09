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

import net.ersei.dml.config
import net.ersei.dml.enums.DataModelTier
import net.ersei.dml.enums.EntityCategory
import net.ersei.dml.event.VanillaEvents
import net.ersei.dml.identifier
import net.ersei.dml.modular_armor.core.ModularEffectContext
import net.ersei.dml.modular_armor.core.ModularEffectTriggerPayload
import net.ersei.dml.modular_armor.core.WrappedEffectTriggerPayload
import net.ersei.dml.modular_armor.data.ModularArmorData
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult

class AutoExtinguishEffect : ProtectionLikeEffect(
    identifier("auto_extinguish"),
    EntityCategory.NETHER,
    config.glitchArmor.costs::autoExtinguish
) {
    override fun registerEvents() {
        VanillaEvents.LivingEntityDamageEvent.register { eventContext ->
            if (eventContext.entity is PlayerEntity) {
                ModularEffectContext.from(eventContext.entity as PlayerEntity)
                    .shuffled()
                    .firstOrNull { effectContext ->
                        attemptToApply(effectContext, ModularEffectTriggerPayload.wrap(eventContext)) { _, _ ->
                            eventContext.entity.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 1F, 1F)
                            eventContext.entity.fireTicks = 0
                        }.result == ActionResult.SUCCESS
                    }
            }
            null
        }
    }

    override fun protectsAgainst(source: DamageSource) = source.isFire

    override fun acceptTier(tier: DataModelTier): Boolean {
        return tier.ordinal >= 2
    }

    // todo check if the player is standing in fire too
    override fun canApply(context: ModularEffectContext, payload: WrappedEffectTriggerPayload<VanillaEvents.LivingEntityDamageContext>): Boolean {
        return super.canApply(context, payload) && !context.player.isInLava
    }

    override fun createEntityAttributeModifier(armor: ModularArmorData): EntityAttributeModifier {
        return EntityAttributeModifier(id.toString(), 1.0, EntityAttributeModifier.Operation.MULTIPLY_BASE)
    }

}
