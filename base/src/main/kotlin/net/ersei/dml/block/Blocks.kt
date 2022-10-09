/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Backported.
 *
 * Deep Mob Learning: Backported is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * Deep Mob Learning: Backported is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Backported.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.ersei.dml.block

import net.ersei.dml.identifier
import net.ersei.dml.item.settings
import net.minecraft.item.BlockItem
import net.minecraft.util.Rarity
import net.minecraft.util.registry.Registry

val BLOCK_TRIAL_KEYSTONE = BlockTrialKeystone()
val BLOCK_LOOT_FABRICATOR = BlockLootFabricator()
val BLOCK_CAFETERIA = BlockCafeteria()

fun registerBlocks() {
    hashMapOf(
        BLOCK_TRIAL_KEYSTONE to "trial_keystone",
        BLOCK_LOOT_FABRICATOR to "loot_fabricator",
        BLOCK_CAFETERIA to "cafeteria"
    ).forEach { (block, id) ->
        val identifier = identifier(id)
        Registry.register(Registry.BLOCK, identifier, block)
        Registry.register(Registry.ITEM, identifier, BlockItem(block, settings().rarity(if(block == BLOCK_CAFETERIA) Rarity.EPIC else Rarity.UNCOMMON))) // This is a bad way to do it, but it's fine for now
    }
}
