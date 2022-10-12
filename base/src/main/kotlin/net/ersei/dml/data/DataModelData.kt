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

package net.ersei.dml.data

import net.ersei.dml.MOD_ID
import net.ersei.dml.NotDataModelException
import net.ersei.dml.enums.DataModelTier
import net.ersei.dml.enums.EntityCategory
import net.ersei.dml.item.ItemDataModel
import net.ersei.ktdatatag.data.MutableCompoundData
import net.ersei.ktdatatag.serializer.Serializers
import net.minecraft.item.ItemStack

class DataModelData(val stack: ItemStack, val category: EntityCategory?) : MutableCompoundData(stack.orCreateNbt) {

    companion object {
        const val DATA_AMOUNT_TAG_KEY = "${MOD_ID}.datamodel.dataAmount"
    }

    var dataAmount by persistentDefaulted(0, Serializers.INT, DATA_AMOUNT_TAG_KEY)

    fun tier() = DataModelTier.fromDataAmount(dataAmount)

}

val ItemStack.dataModel: DataModelData
    get() {
        item.let { item ->
            if (item is ItemDataModel) {
                return DataModelData(this, item.category)
            }
        }
        throw NotDataModelException()
    }
