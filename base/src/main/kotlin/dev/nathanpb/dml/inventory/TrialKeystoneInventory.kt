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

package dev.nathanpb.dml.inventory

import dev.nathanpb.dml.utils.toVec3d
import net.minecraft.entity.ItemEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class TrialKeystoneInventory : SimpleInventory(9), SidedInventory {

    fun dropAll(world: World, pos: BlockPos) {
        val pos3d = pos.toVec3d()
        clearToList().map {
            ItemEntity(world, pos3d.x, pos3d.y, pos3d.z, it)
        }.forEach {
            world.spawnEntity(it)
        }
    }

    override fun canExtract(slot: Int, stack: ItemStack?, dir: Direction?) = false

    override fun canInsert(slot: Int, stack: ItemStack?, dir: Direction?) = false

    override fun getAvailableSlots(side: Direction?) = intArrayOf()
}
