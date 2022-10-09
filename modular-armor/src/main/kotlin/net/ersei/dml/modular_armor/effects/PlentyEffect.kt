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
import net.ersei.dml.modular_armor.core.EffectStackOption
import net.ersei.dml.modular_armor.core.ModularEffect
import net.ersei.dml.modular_armor.core.ModularEffectContext
import net.ersei.dml.modular_armor.core.ModularEffectTriggerPayload
import net.ersei.dml.modular_armor.data.ModularArmorData
import net.ersei.dml.utils.firstOrNullMapping
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.util.ActionResult
import net.minecraft.world.World
import kotlin.random.Random

class PlentyEffect : ModularEffect<ModularEffectTriggerPayload>(
    identifier("plenty"),
    EntityCategory.OVERWORLD,
    config.glitchArmor.costs::plenty
) {
    override fun registerEvents() {
        VanillaEvents.PlayerTakeHungerEvent.register { player, amount ->
            if (!player.world.isClient) {
                return@register ModularEffectContext.from(player)
                    .run(EffectStackOption.PRIORITIZE_GREATER.apply)
                    .firstOrNullMapping({ context ->
                        attemptToApply(context, ModularEffectTriggerPayload.EMPTY) { _, _ ->
                            if (Random.nextFloat() <= sumLevelsOf(context.armor.stack)) {
                                0
                            } else amount
                        }
                    }, { it.result == ActionResult.SUCCESS })?.value ?: amount
            }
            return@register amount
        }
    }

    override fun acceptTier(tier: DataModelTier) = true

    override fun createEntityAttributeModifier(armor: ModularArmorData): EntityAttributeModifier {
        return EntityAttributeModifier(id.toString(), (armor.tier().ordinal.inc() / 100.0) * 15.0, EntityAttributeModifier.Operation.MULTIPLY_BASE)
    }

    override fun canApply(context: ModularEffectContext, payload: ModularEffectTriggerPayload): Boolean {
        val hasSunlight by lazy {
            context.player.world.isDay && context.player.world.isSkyVisible(context.player.blockPos.add(0, 1, 0))
        }

        return context.player.world.registryKey == World.OVERWORLD
                && super.canApply(context, payload)
                && (context.tier.isMaxTier() || hasSunlight)
    }
}
