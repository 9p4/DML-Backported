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

package net.ersei.dml.compat.rei

import net.ersei.dml.block.BLOCK_LOOT_FABRICATOR
import net.ersei.dml.block.BLOCK_TRIAL_KEYSTONE
import net.ersei.dml.compat.rei.category.CrushingDisplayCategory
import net.ersei.dml.compat.rei.category.LootFabricatorDisplayCategory
import net.ersei.dml.compat.rei.category.TrialDisplayCategory
import net.ersei.dml.compat.rei.display.CrushingRecipeDisplay
import net.ersei.dml.compat.rei.display.LootFabricatorRecipeDisplay
import net.ersei.dml.compat.rei.display.TrialRecipeDisplay
import net.ersei.dml.identifier
import net.ersei.dml.item.ITEM_DML
import net.ersei.dml.item.ITEM_EMERITUS_HAT
import net.ersei.dml.recipe.CrushingRecipe
import net.ersei.dml.recipe.LootFabricatorRecipe
import net.ersei.dml.recipe.TrialKeystoneRecipe
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.plugins.PluginManager
import me.shedaniel.rei.api.common.registry.ReloadStage
import me.shedaniel.rei.api.common.util.EntryStacks


@Suppress("unused")
class ReiPlugin :  REIClientPlugin {

    companion object {
        val CRUSHING_CATEGORY: CategoryIdentifier<CrushingRecipeDisplay> = CategoryIdentifier.of(identifier("crushing"))
        val TRIAL_CATEGORY: CategoryIdentifier<TrialRecipeDisplay> = CategoryIdentifier.of(identifier("trial"))
        val LOOT_FABRICATOR_CATEGORY: CategoryIdentifier<LootFabricatorRecipeDisplay> = CategoryIdentifier.of(identifier("loot_fabricator"))

    }


    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(CrushingDisplayCategory())
        //registry.addWorkstations(CRUSHING_CATEGORY, EntryStacks.of(ITEM_SOOT_REDSTONE))

        registry.add(TrialDisplayCategory())
        registry.addWorkstations(TRIAL_CATEGORY, EntryStacks.of(BLOCK_TRIAL_KEYSTONE))

        registry.add(LootFabricatorDisplayCategory())
        registry.addWorkstations(LOOT_FABRICATOR_CATEGORY, EntryStacks.of(BLOCK_LOOT_FABRICATOR))
    }

    override fun registerDisplays(registry: DisplayRegistry){
        registry.registerFiller(CrushingRecipe::class.java) {
            CrushingRecipeDisplay(it)
        }

        registry.registerFiller(TrialKeystoneRecipe::class.java){
            TrialRecipeDisplay(it)
        }

        registry.registerFiller(LootFabricatorRecipe::class.java) {
            LootFabricatorRecipeDisplay(it)
        }
    }

    override fun postStage(manager: PluginManager<REIClientPlugin>?, stage: ReloadStage?) {
        val hiddenItems = listOf(ITEM_EMERITUS_HAT, ITEM_DML)
        EntryRegistry.getInstance().removeEntryIf {
            it.itemStack().item in hiddenItems
        }
    }

}