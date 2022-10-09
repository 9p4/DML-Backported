/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Backported.
 *
 * Deep Mob Learning: Backported is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deep Mob Learning: Backported is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Backported.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.ersei.dml.trial.affix

import net.ersei.dml.config
import net.ersei.dml.enums.DataModelTier
import net.ersei.dml.identifier
import net.ersei.dml.trial.Trial
import net.ersei.dml.trial.affix.core.TrialAffix
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.util.Identifier
import kotlin.random.Random

abstract class PotionEffectTrialAffix(
    id: Identifier,
    @Suppress("private") vararg val effects: StatusEffect
): TrialAffix(id), TrialAffix.WaveSpawnedListener  {

    override fun onWaveSpawn(trial: Trial, waveEntities: List<LivingEntity>) {
        val chanceOfApplying = (trial.recipe.tier.ordinal.inc().toFloat() / DataModelTier.values().size)
        waveEntities.filter {
            Random.nextFloat() <= chanceOfApplying
        }.forEach { entity ->
            effects.map { effect ->
                StatusEffectInstance(effect, (config.trial.maxTime - trial.tickCount).coerceAtLeast(0))
            }.forEach {
                entity.addStatusEffect(it)
            }
        }
    }

}

class MobStrengthTrialAffix : PotionEffectTrialAffix(identifier("mob_strength"), StatusEffects.STRENGTH) {
    override fun isEnabled() = config.affix.enableMobStrength
}

class MobSpeedTrialAffix : PotionEffectTrialAffix(identifier("mob_speed"), StatusEffects.SPEED) {
    override fun isEnabled() = config.affix.enableMobSpeed
}

class MobResistanceTrialAffix : PotionEffectTrialAffix(identifier("mob_resistance"), StatusEffects.RESISTANCE) {
    override fun isEnabled() = config.affix.enableMobResistance
}
