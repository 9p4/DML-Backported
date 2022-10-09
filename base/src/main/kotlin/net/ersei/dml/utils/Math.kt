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

package net.ersei.dml.utils

import kotlin.random.Random

fun Double.lerp(min: Double, max: Double) = (1 - this) * min + this * max
fun Int.lerp(min: Double, max: Double) = (1 - this) * min + this * max

fun Int.squared() = this * this

// https://stackoverflow.com/a/35701777/9893963
fun <T> discreteDistribution(table: Map<T, Float>): T {
  var prob = Random.nextFloat() * table.values.sum()
  var i = 0
  while (prob > 0) {
    prob -= table.values.toList()[i]
    i++
  }
  return table.keys.toList()[i - 1]
}
