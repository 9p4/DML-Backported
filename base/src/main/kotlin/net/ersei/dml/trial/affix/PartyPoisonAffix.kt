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
import net.ersei.dml.identifier
import net.ersei.dml.trial.Trial
import net.ersei.dml.trial.TrialState
import net.ersei.dml.trial.affix.core.TrialAffix
import net.ersei.dml.utils.getPlayersByUUID
import net.ersei.dml.utils.toVec3d
import net.minecraft.entity.projectile.thrown.PotionEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.text.Text
import net.minecraft.text.LiteralText
import kotlin.random.Random

class PartyPoisonAffix : TrialAffix(identifier("party_poison")), TrialAffix.TickableAffix {

    override fun isEnabled() = config.affix.enablePartyPoison

    override fun tick(trial: Trial) {
        if (trial.state == TrialState.RUNNING && Random.nextFloat() < config.affix.partyPoisonChance) {

            (0..360).step(16).forEach { angle ->
                val potionEntity = PotionEntity(trial.world, trial.systemGlitch)
                trial.pos.toVec3d().apply {
                    potionEntity.setPos(x, y+1, z)
                }
                val stack = ItemStack(Items.SPLASH_POTION)
                PotionUtil.setPotion(stack, Potions.POISON)
                potionEntity.setItem(stack)
                potionEntity.setVelocity(trial.systemGlitch, -60F, angle.toFloat(), -20.0f, 0.5f, 1.0f)
                trial.world.spawnEntity(potionEntity)
            }

            if (Random.nextFloat() < .01F) {
                trial.world.getPlayersByUUID(trial.players).forEach {
                    it.sendMessage(LiteralText("Hide your eyes, we're gonna shine tonight"), false)
                }
            }
        }
    }
}
