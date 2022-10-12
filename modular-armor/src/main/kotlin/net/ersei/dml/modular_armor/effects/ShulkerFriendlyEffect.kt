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
import net.ersei.dml.identifier
import net.ersei.dml.modular_armor.core.ModularEffectContext
import net.ersei.dml.modular_armor.core.WrappedEffectTriggerPayload
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity

class ShulkerFriendlyEffect : TargetCancellationEffect(
    identifier("shulker_friendly"),
    EntityCategory.END,
    config.glitchArmor.costs::shulkerFriendly
) {

    override fun acceptTier(tier: DataModelTier) = tier.ordinal >= 3

    override fun canApply(context: ModularEffectContext, payload: WrappedEffectTriggerPayload<LivingEntity>): Boolean {
        return payload.value.type == EntityType.SHULKER && super.canApply(context, payload)
    }
}