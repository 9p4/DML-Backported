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
import net.ersei.dml.item.ItemPristineMatter
import net.ersei.dml.modular_armor.BlockMatterCondenser
import net.ersei.dml.modular_armor.ItemModularGlitchArmor
import net.ersei.dml.modular_armor.data.ModularArmorData
import net.ersei.dml.screen.handler.registerScreenHandlerForBlockEntity
import net.ersei.dml.screen.handler.slot.WTooltippedItemSlot
import net.ersei.dml.utils.RenderUtils
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WBar
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

class MatterCondenserScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    ctx: ScreenHandlerContext
) : SyncedGuiDescription(
    INSTANCE,
    syncId, playerInventory,
    getBlockInventory(ctx),
    getBlockPropertyDelegate(ctx)
) {

    companion object {
        val INSTANCE = registerScreenHandlerForBlockEntity(
            BlockMatterCondenser.IDENTIFIER,
            ::MatterCondenserScreenHandler
        )
    }

    init {
        val root = WPlainPanel()
        setRootPanel(root)
        root.insets = Insets.ROOT_PANEL

        val slots = WPlainPanel()
        val armorSlot = WTooltippedItemSlot.of(blockInventory, 0, TranslatableText("gui.${MOD_ID}.glitch_armor_only")).setFilter {
            it.item is ItemModularGlitchArmor && !ModularArmorData(it).tier().isMaxTier()
        }

        slots.add(armorSlot, 2*18, 2*18)

        val matterSlots = (1..6).map {
            WTooltippedItemSlot.of(blockInventory, it, TranslatableText("gui.${MOD_ID}.pristine_matter_only")).setFilter { stack ->
                stack.item is ItemPristineMatter
            }
        }

        slots.add(matterSlots[0], 2*18, 0*18)
        slots.add(matterSlots[1], 0*18, 1*18)
        slots.add(matterSlots[2], 4*18, 1*18)
        slots.add(matterSlots[3], 0*18, 3*18)
        slots.add(matterSlots[4], 4*18, 3*18)
        slots.add(matterSlots[5], 2*18, 4*18)

        root.add(slots, 2*18, 1*18)
        root.add(this.createPlayerInventoryPanel(), 0*18, 6*18+6)

        val progressBar1 = WBar(RenderUtils.PROGRESS_BAR_BACKGROUND, RenderUtils.PROGRESS_BAR, 0, 1, WBar.Direction.UP)
        val progressBar2 = WBar(RenderUtils.PROGRESS_BAR_BACKGROUND, RenderUtils.PROGRESS_BAR, 0, 1, WBar.Direction.UP)
        root.add(progressBar1, 0*18, 1*18, 1*18, 5*18)
        root.add(progressBar2, 8*18, 1*18, 1*18, 5*18)

        root.validate(this)

        (blockInventory as? SimpleInventory)?.addListener {
            sendContentUpdates()
        }
    }

    override fun addPainters() {
        rootPanel.backgroundPainter = RenderUtils.DEFAULT_BACKGROUND_PAINTER
    }

    override fun getTitleColor(): Int {
        return RenderUtils.getDefaultTextColor(world)
    }

}
