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

import net.ersei.dml.enums.EntityCategory
import net.ersei.dml.event.VanillaEvents
import net.ersei.dml.modular_armor.core.ModularEffectContext
import net.ersei.dml.modular_armor.core.ModularEffectTriggerPayload
import net.ersei.dml.modular_armor.core.WrappedEffectTriggerPayload
import net.ersei.dml.utils.takeOrNull
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class DamageImmunityLikeEffect(
    id: Identifier,
    category: EntityCategory,
    applyCost: ()->Float
) : ProtectionLikeEffect(id, category, applyCost) {

    override fun registerEvents() {
        VanillaEvents.LivingEntityDamageEvent.register { eventContext ->
            takeOrNull(eventContext.entity is PlayerEntity) {
                ModularEffectContext.from(eventContext.entity as PlayerEntity)
                    .shuffled()
                    .firstOrNull { effectContext ->
                        attemptToApply(effectContext, ModularEffectTriggerPayload.wrap(eventContext)) == ActionResult.SUCCESS
                    }?.let {
                        eventContext.copy(damage = 0F)
                    }
            }
        }
    }

    override fun canApply(context: ModularEffectContext, payload: WrappedEffectTriggerPayload<VanillaEvents.LivingEntityDamageContext>): Boolean {
        return super.canApply(context, payload) && sumLevelsOf(context.armor.stack) > 0
    }
}
