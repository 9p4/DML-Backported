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

package net.ersei.dml.screen.handler

import net.ersei.dml.identifier
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Hand
import net.minecraft.util.Identifier

val HANDLER_LOOT_FABRICATOR = registerScreenHandlerForBlockEntity(identifier("loot_fabricator"), ::LootFabricatorHandler)
val HANDLER_DEEP_LEARNER = registerScreenHandlerForItemStack(identifier("deep_learner"), ::DeepLearnerScreenHandler)

// TODO: Replace with non-deprecated version
fun <T: ScreenHandler>registerScreenHandlerForBlockEntity(
    id: Identifier,
    f: (Int, PlayerInventory, ScreenHandlerContext) -> T
): ExtendedScreenHandlerType<T> {
    return ScreenHandlerRegistry.registerExtended(id) { syncId, inventory, buf ->
        f(syncId, inventory, ScreenHandlerContext.create(inventory.player.world, buf.readBlockPos()))
    } as ExtendedScreenHandlerType<T>
}

fun <T: ScreenHandler>registerScreenHandlerForItemStack(
    id: Identifier,
    f: (Int, PlayerInventory, Hand) -> T
): ExtendedScreenHandlerType<T> {
    return ScreenHandlerRegistry.registerExtended(id) { syncId, inventory, buf ->
        f(syncId, inventory, Hand.values()[buf.readInt()])
    } as ExtendedScreenHandlerType<T>
}

fun registerScreenHandlers() {
    HANDLER_LOOT_FABRICATOR
    HANDLER_DEEP_LEARNER
}
