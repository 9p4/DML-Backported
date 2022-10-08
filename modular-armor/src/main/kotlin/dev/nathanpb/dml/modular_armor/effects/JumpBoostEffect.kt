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

package dev.nathanpb.dml.modular_armor.effects

import dev.nathanpb.dml.config
import dev.nathanpb.dml.enums.DataModelTier
import dev.nathanpb.dml.enums.EntityCategory
import dev.nathanpb.dml.identifier
import dev.nathanpb.dml.modular_armor.core.EffectStackOption
import dev.nathanpb.dml.modular_armor.core.ModularEffectContext
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.text.Text
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

class JumpBoostEffect : StatusEffectLikeEffect(
    identifier("jump_boost"),
    EntityCategory.SLIMY,
    config.glitchArmor.costs::jumpBoost,
    EffectStackOption.PRIORITIZE_GREATER
) {

    override val name = TranslatableText("effect.minecraft.jump_boost")

    override fun createEffectInstance(context: ModularEffectContext): StatusEffectInstance {
        return StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 17, context.tier.ordinal / 2, false, false)
    }

    override fun acceptTier(tier: DataModelTier): Boolean {
        return tier.ordinal >= 2
    }
}
