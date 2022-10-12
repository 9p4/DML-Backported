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
import net.ersei.dml.data.serializers.TrialAffixListSerializer
import net.ersei.dml.enums.DataModelTier
import net.ersei.dml.enums.EntityCategory
import net.ersei.dml.utils.takeOrNull
import net.ersei.ktdatatag.data.MutableCompoundData
import net.ersei.ktdatatag.serializer.EnumSerializer
import net.ersei.ktdatatag.serializer.Serializers
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

class TrialKeyData (tag: NbtCompound) : MutableCompoundData(tag) {

    constructor(stack: ItemStack) : this(stack.getOrCreateSubNbt(TAG_KEY))

    companion object {
        const val TAG_KEY = "${MOD_ID}.trialkey"

        fun fromDataModelData(data: DataModelData) = data.category?.let {
            TrialKeyData(NbtCompound()).apply {
                category = it
                dataAmount = data.dataAmount
             }
        }
    }

    var category by persistentDefaulted(EntityCategory.END, EnumSerializer(EntityCategory::class.java))
    var dataAmount by persistentDefaulted(0, Serializers.INT)
    var affixes by persistentDefaulted(emptyList(), TrialAffixListSerializer())

    fun tier() = DataModelTier.fromDataAmount(dataAmount)

}

var ItemStack.trialKeyData : TrialKeyData?
    get() = takeOrNull(orCreateNbt.contains(TrialKeyData.TAG_KEY)) {
        TrialKeyData(this)
    }
    set(value) = if (value != null) {
        this.setSubNbt(TrialKeyData.TAG_KEY, value.tag)
    } else {
        this.removeSubNbt(TrialKeyData.TAG_KEY)
    }
