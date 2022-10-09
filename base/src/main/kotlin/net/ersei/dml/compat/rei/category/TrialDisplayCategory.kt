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

package net.ersei.dml.compat.rei.category

import net.ersei.dml.MOD_ID
import net.ersei.dml.block.BLOCK_TRIAL_KEYSTONE
import net.ersei.dml.compat.rei.ReiPlugin
import net.ersei.dml.compat.rei.display.TrialRecipeDisplay
import net.ersei.dml.compat.rei.widgets.EntityDisplayWidget
import net.ersei.dml.entity.SYSTEM_GLITCH_ENTITY_TYPE
import net.ersei.dml.entity.SystemGlitchEntity
import net.ersei.dml.item.ITEM_TRIAL_KEY
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

class TrialDisplayCategory: DisplayCategory<TrialRecipeDisplay> {

    override fun getIdentifier(): Identifier = ReiPlugin.TRIAL_CATEGORY.identifier

    override fun getIcon(): EntryStack<ItemStack> = EntryStacks.of(ITEM_TRIAL_KEY)

    override fun getCategoryIdentifier(): CategoryIdentifier<TrialRecipeDisplay> = ReiPlugin.TRIAL_CATEGORY

    override fun getTitle(): MutableText = TranslatableText("rei.$MOD_ID.category.trial")


    override fun setupDisplay(recipeDisplay: TrialRecipeDisplay, bounds: Rectangle): MutableList<Widget> {
        val centerX = bounds.centerX - 5
        val centerY = bounds.centerY

        val input = recipeDisplay.inputEntries[0]
        val output = recipeDisplay.outputEntries
            .withIndex()
            .groupBy { (index, _) -> index % 5 }
            .map { it -> it.value.map { it.value } }

        val keySlot = Widgets.createSlot(Point(centerX, centerY - 24)).entries(input)

        val keystoneSlot = Widgets.createSlot(Point(centerX, centerY - 8))
            .entries(mutableListOf(EntryStacks.of(BLOCK_TRIAL_KEYSTONE.asItem())))
            .apply {
                isBackgroundEnabled = false
            }

        val outputSlots = output.mapIndexed { index, stacks ->
            val x = (centerX + (index * 18)) - (output.size.dec() * 9)
            Widgets.createSlot(Point(x, centerY + 12))
                .entries(stacks.flatten())
        }

        return mutableListOf<Widget>(
            Widgets.createRecipeBase(bounds),
            *outputSlots.toTypedArray(),
            keySlot,
            keystoneSlot
        ).also { widgets ->
            MinecraftClient.getInstance().player?.let { player ->
                widgets += Widgets.wrapVanillaWidget(EntityDisplayWidget(
                    listOf(player),
                    centerX - 30,
                    centerY + 8,
                    -120F, 20F, 16,
                    ItemStack(Items.DIAMOND_SWORD) // fuck netherite
                ))

                widgets += Widgets.wrapVanillaWidget(EntityDisplayWidget(
                    listOf(SystemGlitchEntity(SYSTEM_GLITCH_ENTITY_TYPE, player.world)),
                    centerX + 45,
                    centerY + 8,
                    120F, 20F, 16
                ))
            }
        }
    }
}
