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

package net.ersei.dml.compat.rei.display

import net.ersei.dml.compat.rei.ReiPlugin
import net.ersei.dml.recipe.CrushingRecipe
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients

class CrushingRecipeDisplay(
    private val recipe: CrushingRecipe
) : Display {


    override fun getCategoryIdentifier(): CategoryIdentifier<CrushingRecipeDisplay> = ReiPlugin.CRUSHING_CATEGORY

    override fun getInputEntries(): MutableList<EntryIngredient> {
        return (
            recipe.input.matchingStacks.map(EntryIngredients::of)
                + arrayOf(EntryIngredients.of(recipe.block))
        ).toMutableList()
    }

    override fun getOutputEntries() = mutableListOf(EntryIngredients.of(recipe.output))

}
