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
import net.ersei.dml.modular_armor.core.*
import net.ersei.dml.modular_armor.data.ModularArmorData
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import kotlin.math.sqrt

abstract class TargetCancellationEffect(
    id: Identifier,
    category: EntityCategory,
    applyCost: () -> Float
) : ModularEffect<WrappedEffectTriggerPayload<LivingEntity>>(id, category, applyCost) {

    companion object {
        private val INSTANCES by lazy {
            ModularEffectRegistry.INSTANCE.all.filterIsInstance<TargetCancellationEffect>()
        }

        fun attemptToCancel(mob: MobEntity, target: LivingEntity): ActionResult {
            INSTANCES.forEach { instance ->
                if (target is PlayerEntity && !target.world.isClient) {
                    ModularEffectContext.from(target)
                        .run(EffectStackOption.RANDOMIZE.apply)
                        .any {
                            instance.attemptToApply(it, ModularEffectTriggerPayload.wrap(mob)) == ActionResult.SUCCESS
                        }.let {
                            if (it) {
                                return ActionResult.FAIL
                            }
                        }
                }
            }
            return ActionResult.PASS
        }
    }

    override fun registerEvents() {
    }

    override fun createEntityAttributeModifier(armor: ModularArmorData): EntityAttributeModifier {
        return EntityAttributeModifier(id.toString(), 1.0, EntityAttributeModifier.Operation.MULTIPLY_BASE)
    }

    // Make sure it will only cancel the target when absolutely needed
    override fun canApply(context: ModularEffectContext, payload: WrappedEffectTriggerPayload<LivingEntity>): Boolean {
        val entity = payload.value
        return entity.isAlive
            && entity.canTarget(EntityType.PLAYER)
            && super.canApply(context, payload)
            && sqrt(entity.distanceTo(context.player)) <= entity.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)
    }

    // Data draining goes brrrrrr
    override fun shouldConsumeData(context: ModularEffectContext): Boolean {
        return super.shouldConsumeData(context) && context.player.world.time % 20 == 0L
    }

}
