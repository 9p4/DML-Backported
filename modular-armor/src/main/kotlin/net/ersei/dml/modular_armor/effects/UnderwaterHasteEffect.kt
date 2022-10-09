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

import net.ersei.dml.MOD_ID
import net.ersei.dml.config
import net.ersei.dml.enums.DataModelTier
import net.ersei.dml.enums.EntityCategory
import net.ersei.dml.identifier
import net.ersei.dml.modular_armor.UNDERWATER_HASTE_EFFECT
import net.ersei.dml.modular_armor.core.EffectStackOption
import net.ersei.dml.modular_armor.core.ModularEffectContext
import net.ersei.dml.modular_armor.core.ModularEffectTriggerPayload
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.tag.FluidTags
import net.minecraft.text.Text
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

class UnderwaterHasteEffect : StatusEffectLikeEffect(
    identifier("underwater_haste"),
    EntityCategory.OCEAN,
    config.glitchArmor.costs::underwaterHaste,
    EffectStackOption.PRIORITIZE_GREATER
) {

    override val name = TranslatableText("effect.${MOD_ID}.underwater_haste")

    override fun createEffectInstance(context: ModularEffectContext): StatusEffectInstance {
        return StatusEffectInstance(UNDERWATER_HASTE_EFFECT, 16 * 20, context.tier.ordinal / 2, true, false)
    }

    override fun acceptTier(tier: DataModelTier) = true

    override fun canApply(context: ModularEffectContext, payload: ModularEffectTriggerPayload): Boolean {
        return super.canApply(context, payload) && context.player.isSubmergedIn(FluidTags.WATER)
    }

}
