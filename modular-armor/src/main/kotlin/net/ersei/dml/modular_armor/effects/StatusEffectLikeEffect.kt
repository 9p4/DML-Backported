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
import net.ersei.dml.modular_armor.core.EffectStackOption
import net.ersei.dml.modular_armor.core.ModularEffect
import net.ersei.dml.modular_armor.core.ModularEffectContext
import net.ersei.dml.modular_armor.core.ModularEffectTriggerPayload
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class StatusEffectLikeEffect(
    id: Identifier,
    category: EntityCategory,
    applyCost: ()->Float,
    val stackingOption: EffectStackOption
) : ModularEffect<ModularEffectTriggerPayload>(id, category, applyCost) {

    override fun registerEvents() {
        VanillaEvents.PlayerEntityTickEvent.register { player ->
            if (player.world.time % 80 == 0L) {
                ModularEffectContext.from(player)
                    .run(stackingOption.apply)
                    .firstOrNull {
                        attemptToApply(it, ModularEffectTriggerPayload.EMPTY) { context, _ ->
                            player.addStatusEffect(createEffectInstance(context))
                        }.result == ActionResult.SUCCESS
                    }
            }
        }
    }

    abstract fun createEffectInstance(context: ModularEffectContext): StatusEffectInstance

    override fun canApply(context: ModularEffectContext, payload: ModularEffectTriggerPayload): Boolean {
        val doesNotHasEffect by lazy {
            val statusEffectInstance = createEffectInstance(context)
            context.player.statusEffects.none {
                it.duration < 15 * 20
                    && it.effectType == statusEffectInstance.effectType
                    && it.amplifier < statusEffectInstance.amplifier
            }
        }

        return super.canApply(context, payload)
            && doesNotHasEffect
            && sumLevelsOf(context.armor.stack) > 0
    }

}
