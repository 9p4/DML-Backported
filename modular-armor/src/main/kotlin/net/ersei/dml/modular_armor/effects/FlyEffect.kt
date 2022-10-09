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
import net.ersei.dml.modular_armor.core.ModularEffectContext
import net.ersei.dml.modular_armor.core.ModularEffectTriggerPayload
import net.ersei.dml.modular_armor.data.ModularArmorData
import net.ersei.dml.modular_armor.flightBurnoutManager
import io.github.ladysnake.pal.VanillaAbilities
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.util.ActionResult

class FlyEffect : AbilityBasedEffect(
    identifier("fly"),
    EntityCategory.GHOST,
    config.glitchArmor.costs::fly,
    VanillaAbilities.ALLOW_FLYING
) {
    override fun registerEvents() {
        super.registerEvents()
        VanillaEvents.PlayerEntityTickEvent.register { player ->
            // Check if the data should be consumed
            if (
                !player.world.isClient
                && player.world.time % 20 == 0L
                && abilitySource.grants(player, ability)
                && player.world.getBlockState(player.blockPos.down()).isAir
            ) {
                ModularEffectContext.from(player)
                    .run(EffectStackOption.PRIORITIZE_GREATER.apply)
                    .firstOrNull { context ->
                        attemptToApply(context, ModularEffectTriggerPayload.EMPTY) == ActionResult.SUCCESS
                    }
            }
        }
    }

    override fun createEntityAttributeModifier(armor: ModularArmorData): EntityAttributeModifier {
        return EntityAttributeModifier(id.toString(), 1.0, EntityAttributeModifier.Operation.ADDITION)
    }

    override fun acceptTier(tier: DataModelTier) = tier.isMaxTier()

    override fun canApply(context: ModularEffectContext, payload: ModularEffectTriggerPayload): Boolean {
        return super.canApply(context, payload) && context.player.flightBurnoutManager.canFly
    }

}
