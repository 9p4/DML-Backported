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

package net.ersei.dml.screen

import net.ersei.dml.screen.handler.HANDLER_DEEP_LEARNER
import net.ersei.dml.screen.handler.HANDLER_LOOT_FABRICATOR
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.minecraft.client.gui.screen.ingame.HandledScreens

fun registerScreens() {
    HandledScreens.register(HANDLER_LOOT_FABRICATOR) { handler, inventory, title ->
        CottonInventoryScreen(handler, inventory.player, title)
    }

    HandledScreens.register(HANDLER_DEEP_LEARNER) { handler, inventory, title ->
        CottonInventoryScreen(handler, inventory.player, title)
    }
}
