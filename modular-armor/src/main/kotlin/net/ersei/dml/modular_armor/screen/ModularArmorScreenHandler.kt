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

package net.ersei.dml.modular_armor.screen

import net.ersei.dml.MOD_ID
import net.ersei.dml.data.dataModel
import net.ersei.dml.identifier
import net.ersei.dml.item.ItemDataModel
import net.ersei.dml.modular_armor.core.ModularEffect
import net.ersei.dml.modular_armor.core.ModularEffectRegistry
import net.ersei.dml.modular_armor.data.ModularArmorData
import net.ersei.dml.modular_armor.net.C2S_MODULAR_EFFECT_TOGGLE
import net.ersei.dml.screen.handler.registerScreenHandlerForItemStack
import net.ersei.dml.screen.handler.slot.WTooltippedItemSlot
import net.ersei.dml.utils.RenderUtils
import net.ersei.dml.utils.takeOrNull
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WListPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.text.Text
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import net.minecraft.util.Hand
import net.minecraft.util.Identifier

class ModularArmorScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    private val hand: Hand
): SyncedGuiDescription(
    INSTANCE,
    syncId,
    playerInventory,
    SimpleInventory(ModularArmorData(playerInventory.player.getStackInHand(hand)).dataModel?.stack ?: ItemStack.EMPTY),
    ArrayPropertyDelegate(1)
) {

    companion object {
        val INSTANCE = registerScreenHandlerForItemStack(
            identifier("modular_armor"),
            ::ModularArmorScreenHandler
        )
    }

    val stack: ItemStack
        get() = playerInventory.player.getStackInHand(hand)

    val data
        get() = ModularArmorData(stack)

    init {
        val root = WGridPanel()
        root.insets = Insets.ROOT_PANEL
        setRootPanel(root)

        var lastEffectsList: WListPanel<ModularEffect<*>, WModularEffectToggle>? = null
        fun updateEffectsList() {
            val disabledEffects = data.disabledEffects
            root.remove(lastEffectsList)
            lastEffectsList = WListPanel(getPossibleEffects(), {
                WModularEffectToggle(world).apply {
                    setOnToggle { flag ->
                        effect?.id?.let {
                            sendToggleUpdate(it, flag)
                        }
                    }
                }
            }) { effect, widget ->
                widget.effect = effect
                widget.toggle = effect.id !in disabledEffects
            }

            root.add(lastEffectsList, 1, 1, 8, 4)
            lastEffectsList!!.validate(this)
        }

        val dataModelSlot = WTooltippedItemSlot.of(blockInventory, 0, TranslatableText("gui.${MOD_ID}.data_model_only")).apply {
            setFilter {
                it.isEmpty || (
                        (it.item as? ItemDataModel)?.category != null
                                && data.tier().ordinal >= it.dataModel.tier().ordinal
                        )
            }
            addChangeListener { _, _, _, _ -> updateEffectsList()}
        }

        root.add(dataModelSlot, 0, 2)

        root.add(this.createPlayerInventoryPanel(), 0, 5)
        root.validate(this)

        (blockInventory as? SimpleInventory)?.addListener {
            val stack = blockInventory.getStack(0)
            data.dataModel = takeOrNull(stack.item is ItemDataModel, stack::dataModel)
            sendContentUpdates()
        }
    }

    private fun getPossibleEffects() : List<ModularEffect<*>> {
        val category = data.dataModel?.category ?: return emptyList()
        return ModularEffectRegistry.INSTANCE.allMatching(category, data.tier())
            .filter { it.isEnabled() }
    }

    private fun sendToggleUpdate(effectId: Identifier, flag: Boolean) {
        ClientPlayNetworking.send(C2S_MODULAR_EFFECT_TOGGLE,
            PacketByteBuf(Unpooled.buffer()).apply {
                writeIdentifier(effectId)
                writeBoolean(flag)
                writeInt(hand.ordinal)
            }
        )
    }

    override fun addPainters() {
        rootPanel.backgroundPainter = RenderUtils.DEFAULT_BACKGROUND_PAINTER
    }

    override fun getTitleColor(): Int {
        return RenderUtils.getDefaultTextColor(world)
    }

}