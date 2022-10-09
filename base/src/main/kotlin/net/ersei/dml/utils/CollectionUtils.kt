/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
 */

package net.ersei.dml.utils

import kotlin.math.abs

// https://stackoverflow.com/a/62614583/9893963
fun Iterable<Int>.closestValue(value: Int, default: Int = 0) = minByOrNull { abs(value - it) } ?: default

fun <T, R> Iterable<T>.firstNonNullMapping(map: (T) -> R?): R? {
    for (element in this) {
        return map(element) ?: continue
    }
    return null
}

fun <T, R> Iterable<T>.firstOrNullMapping(map: (T) -> R, accept: (R)->Boolean): R? {
    for (element in this) {
        val transformed = map(element)
        if (accept(transformed)) {
            return transformed
        }
    }
    return null
}

inline fun <reified T> Iterable<*>.firstInstanceOrNull(): T? {
    return firstOrNull { it is T } as T?
}
