/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
 */

package dev.nathanpb.dml.compat.rei

import dev.nathanpb.dml.compat.rei.category.CrushingRecipeCategory
import dev.nathanpb.dml.compat.rei.category.TrialRecipeCategory
import dev.nathanpb.dml.compat.rei.display.CrushingRecipeDisplay
import dev.nathanpb.dml.compat.rei.display.TrialRecipeDisplay
import dev.nathanpb.dml.identifier
import dev.nathanpb.dml.item.ITEM_SOOT_REDSTONE
import dev.nathanpb.dml.item.ITEM_TRIAL_KEY
import dev.nathanpb.dml.recipe.CrushingRecipe
import dev.nathanpb.dml.recipe.TrialKeystoneRecipe
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeHelper
import me.shedaniel.rei.api.plugins.REIPluginV0

@Suppress("unused")
class ReiPlugin : REIPluginV0 {

    companion object {
        val CRUSHING_ID = identifier("crushing")
        val TRIAL_ID = identifier("trial")

        val CRUSHING_LOGO: EntryStack = EntryStack.create(ITEM_SOOT_REDSTONE)
        val TRIAL_LOGO: EntryStack = EntryStack.create(ITEM_TRIAL_KEY)
    }

    override fun getPluginIdentifier() = identifier("rei_compat")

    override fun registerPluginCategories(recipeHelper: RecipeHelper?) {
        recipeHelper?.registerCategory(CrushingRecipeCategory(CRUSHING_ID, CRUSHING_LOGO))
        recipeHelper?.registerCategory(TrialRecipeCategory(TRIAL_ID, TRIAL_LOGO))
    }

    override fun registerRecipeDisplays(recipeHelper: RecipeHelper?) {
        recipeHelper?.registerRecipes(CRUSHING_ID, CrushingRecipe::class.java) {
            CrushingRecipeDisplay(it, CRUSHING_ID)
        }

        recipeHelper?.registerRecipes(TRIAL_ID, TrialKeystoneRecipe::class.java){
            TrialRecipeDisplay(TRIAL_ID, it)
        }
    }

    override fun registerOthers(recipeHelper: RecipeHelper?) {
        recipeHelper?.registerWorkingStations(CRUSHING_ID, CRUSHING_LOGO)
        recipeHelper?.registerWorkingStations(TRIAL_ID, TRIAL_LOGO)
    }
}
