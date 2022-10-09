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
import io.github.ladysnake.pal.AbilitySource
import io.github.ladysnake.pal.Pal
import io.github.ladysnake.pal.PlayerAbility
import net.minecraft.util.Identifier

abstract class AbilityBasedEffect(
    id: Identifier,
    category: EntityCategory,
    applyCost: () -> Float,
    val ability: PlayerAbility
) : ModularEffect<ModularEffectTriggerPayload>(id, category, applyCost) {

    val abilitySource: AbilitySource = Pal.getAbilitySource(id)

    override fun registerEvents() {
        VanillaEvents.PlayerEntityTickEvent.register { player ->

            // Checking if the player should have the ability
            // This does not consume any data
            if (!player.world.isClient && player.world.time % 20 == 0L) {
                ModularEffectContext.from(player)
                    .run(EffectStackOption.PRIORITIZE_GREATER.apply)
                    .any { context ->
                        canApply(context, ModularEffectTriggerPayload.EMPTY)
                    }.let { shallApply ->
                        val has = abilitySource.grants(player, ability)
                        if (shallApply && !has) {
                            abilitySource.grantTo(player, ability)
                        } else if (!shallApply && has) {
                            abilitySource.revokeFrom(player, ability)
                        }
                    }
            }
        }
    }

}
