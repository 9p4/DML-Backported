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

package net.ersei.dml.listener

import net.ersei.dml.config
import net.ersei.dml.data.DeepLearnerData
import net.ersei.dml.data.dataModel
import net.ersei.dml.item.ItemDataModel
import net.ersei.dml.item.ItemDeepLearner
import net.ersei.dml.utils.firstOrNullMapping
import net.ersei.dml.utils.hotbar
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

class DataCollectListener : ServerEntityCombatEvents.AfterKilledOtherEntity {

    // TODO untested code, check if this even work
    override fun afterKilledOtherEntity(world: ServerWorld, player: Entity, entity: LivingEntity) {
        if (player !is ServerPlayerEntity) {
            return
        }

        (player.inventory.hotbar() + player.offHandStack)
            .filter { it.item is ItemDeepLearner }
            .map { DeepLearnerData(it).inventory }
            .flatten()
            .filter { it.item is ItemDataModel }
            .firstOrNullMapping(
                map = { it.dataModel },
                accept = { entity.type.isIn(it.category?.tagKey) && !it.tier().isMaxTier() }
            )?.let {
                it.dataAmount += config.dataCollection.baseDataGainPerKill
            }
    }
}
