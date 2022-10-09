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

package net.ersei.dml.trial.affix.core

import net.ersei.dml.event.ModEvents
import net.ersei.dml.trial.affix.*
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

class TrialAffixRegistry private constructor() {
    companion object {
        @Suppress("private")
        val INSTANCE = TrialAffixRegistry()

        @ApiStatus.Internal
        fun registerDefaultAffixes() {
            INSTANCE.register(MobStrengthTrialAffix())
            INSTANCE.register(MobSpeedTrialAffix())
            INSTANCE.register(MobResistanceTrialAffix())
            INSTANCE.register(ThunderstormAffix())
            INSTANCE.register(PartyPoisonAffix())
        }
    }

    private val registry = mutableListOf<TrialAffix>()

    @Suppress("private", "unused")
    val all: List<TrialAffix>
        get() = registry.toList()

    @Suppress("private")
    fun register(affix: TrialAffix) {
        if (!isRegistered(affix.id)) {
            registry += affix
            if (affix is TrialAffix.WaveSpawnedListener) {
                ModEvents.TrialWaveSpawnEvent.register { trial, entities ->
                    affix.attemptToInvoke(trial) {
                        affix.onWaveSpawn(trial, entities)
                    }
                }
            }
        } else {
            throw DuplicatedRegistryException(affix.id)
        }
    }

    @Suppress("private")
    fun isRegistered(id: Identifier) = registry.any {
        it.id == id
    }

    @Suppress("private", "unused")
    fun findById(id: Identifier) = registry.firstOrNull {
        it.id == id
    }

    fun pickRandomEnabled() = registry.filter(TrialAffix::isEnabled).randomOrNull()
}
