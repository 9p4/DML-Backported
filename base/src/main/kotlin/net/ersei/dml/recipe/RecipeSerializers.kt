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

package net.ersei.dml.recipe

import net.ersei.dml.identifier
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.registry.Registry

lateinit var TRIAL_KEY_ATTUNEMENT_SERIALIZER: TrialKeyAttunementRecipeSerializer
lateinit var TRIAL_KEYSTONE_RECIPE_SERIALIZER: TrialKeystoneRecipe.Serializer
lateinit var CRUSHING_RECIPE_SERIALIZER: CrushingRecipe.Serializer
lateinit var LOOT_FABRICATOR_SERIALIZER: LootFabricatorRecipe.Serializer

private fun <S: RecipeSerializer<T>, T: Recipe<*>>register(id: String, serializer: S) = Registry.register(
    Registry.RECIPE_SERIALIZER,
    identifier(id).toString(),
    serializer
)

fun registerRecipeSerializers() {
    TRIAL_KEY_ATTUNEMENT_SERIALIZER = register("trial_key_attune", TrialKeyAttunementRecipeSerializer())
    TRIAL_KEYSTONE_RECIPE_SERIALIZER = register("trial_keystone", TrialKeystoneRecipe.Serializer())
    CRUSHING_RECIPE_SERIALIZER = register("crushing", CrushingRecipe.Serializer())
    LOOT_FABRICATOR_SERIALIZER = register("loot_fabricator", LootFabricatorRecipe.Serializer())
}
