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

package net.ersei.dml.modular_armor.data

import net.ersei.dml.MOD_ID
import net.ersei.dml.config
import net.ersei.dml.data.DataModelData
import net.ersei.dml.data.dataModel
import net.ersei.dml.enums.DataModelTier
import net.ersei.dml.item.ItemDataModel
import net.ersei.dml.utils.takeOrNull
import net.ersei.ktdatatag.data.MutableCompoundData
import net.ersei.ktdatatag.serializer.Serializers
import net.minecraft.item.ItemStack
import kotlin.math.max

class ModularArmorData(val stack: ItemStack) : MutableCompoundData(stack.orCreateNbt) {

    companion object {
        fun amountRequiredTo(tier: DataModelTier) = when (tier) {
            DataModelTier.FAULTY -> 0
            DataModelTier.BASIC -> config.glitchArmor.dataAmountToBasic
            DataModelTier.ADVANCED -> config.glitchArmor.dataAmountToAdvanced
            DataModelTier.SUPERIOR -> config.glitchArmor.dataAmountToSuperior
            DataModelTier.SELF_AWARE -> config.glitchArmor.dataAmountToSelfAware
        }
    }

    var dataAmount by persistentDefaulted(0, Serializers.INT, "${MOD_ID}.dataAmount")

    var disabledEffects by persistentDefaulted(emptyList(), Serializers.IDENTIFIER_LIST)

    private var dataModelStack by persistentDefaulted(ItemStack.EMPTY, Serializers.ITEM_STACK, "${MOD_ID}.dataModel")

    var dataModel: DataModelData?
        get() {
            val stack = dataModelStack
            return takeOrNull(!stack.isEmpty && (stack.item as? ItemDataModel)?.category != null) {
                stack.dataModel
            }
        }
        set(value) {
            dataModelStack = value?.stack ?: ItemStack.EMPTY
        }

    fun tier() = DataModelTier.values().last {
        amountRequiredTo(it) <= max(dataAmount, 0)
    }

    fun dataRemainingToNextTier(): Int {
        return amountRequiredTo(tier().nextTierOrCurrent()) - dataAmount
    }
}
