package net.ersei.dml.data

import net.ersei.dml.data.serializers.InventorySerializer
import net.ersei.ktdatatag.data.MutableCompoundData
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

class DeepLearnerData(val stack: ItemStack) : MutableCompoundData(stack.orCreateNbt) {

    var inventory by persistentDefaulted(
        DefaultedList.ofSize(4, ItemStack.EMPTY),
        InventorySerializer(4)
    )

}
