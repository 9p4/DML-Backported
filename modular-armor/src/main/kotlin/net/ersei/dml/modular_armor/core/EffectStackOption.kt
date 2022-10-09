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

package net.ersei.dml.modular_armor.core

import kotlin.random.Random

enum class EffectStackOption(val apply: List<ModularEffectContext>.()->List<ModularEffectContext>) {
    PRIORITIZE_GREATER({
        sortedWith(
            compareByDescending<ModularEffectContext> {
                it.tier.ordinal
            }.thenBy { Random.nextInt() }
        )
    }),
    PRIORITIZE_WORST({
        sortedWith(compareBy({ it.tier.ordinal }, { Random.nextInt() }))
    }),
    RANDOMIZE(List<ModularEffectContext>::shuffled),
}
