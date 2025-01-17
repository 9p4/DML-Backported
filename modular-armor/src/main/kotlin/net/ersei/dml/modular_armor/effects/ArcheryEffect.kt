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
import net.ersei.dml.modular_armor.core.*
import net.ersei.dml.utils.firstInstanceOrNull
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import kotlin.math.roundToInt

class ArcheryEffect : ModularEffect<ModularEffectTriggerPayload>(
    identifier("archery"),
    EntityCategory.SKELETON,
    config.glitchArmor.costs::archery
) {

    companion object {

        private val INSTANCE by lazy {
            ModularEffectRegistry.INSTANCE.all.firstInstanceOrNull<ArcheryEffect>()
        }

        private fun levels(player: PlayerEntity): Int {
            return ModularEffectContext.from(player)
                .run(EffectStackOption.PRIORITIZE_GREATER.apply)
                .firstOrNull {
                    INSTANCE?.canApply(it, ModularEffectTriggerPayload.EMPTY) == true
                }?.let {
                    INSTANCE?.sumLevelsOf(it.armor.stack)?.roundToInt()?.coerceAtMost(5)
                } ?: 0
        }

        fun crossbowFastpullReducedTicks(player: PlayerEntity): Int {
            return (levels(player) * 2.5).roundToInt()
        }

        fun bowFastpullLevels(player: PlayerEntity) : Float {
            return levels(player) / 2.5F
        }

    }

    override fun registerEvents() {
        fun trigger(player: LivingEntity, stack: ItemStack) {
            if (player is PlayerEntity && !player.world.isClient) {
                ModularEffectContext.from(player)
                    .run(EffectStackOption.PRIORITIZE_GREATER.apply)
                    .any {
                        attemptToApply(it, ModularEffectTriggerPayload.EMPTY) == ActionResult.SUCCESS
                    }
            }
        }

        VanillaEvents.BowShotEvent.register(::trigger)
        VanillaEvents.CrossbowReloadedEvent.register(::trigger)
    }

    override fun acceptTier(tier: DataModelTier) = true

}
