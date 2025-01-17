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

import com.google.gson.JsonObject
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.registry.Registry

class TrialKeyAttunementRecipeSerializer : RecipeSerializer<TrialKeyAttuneRecipe> {

    override fun write(buf: PacketByteBuf, recipe: TrialKeyAttuneRecipe) {
        buf.writeInt(recipe.ingredients.size)
        recipe.ingredients.forEach {
            it.write(buf)
        }
        buf.writeItemStack(recipe.output)
    }

    override fun read(id: Identifier, json: JsonObject): TrialKeyAttuneRecipe {
        val ingredients = json.getAsJsonArray("ingredients").let { jsonArray ->
            DefaultedList.ofSize(jsonArray.size(), Ingredient.EMPTY).also { list ->
                jsonArray
                    .map(Ingredient::fromJson)
                    .forEachIndexed { index, ingredient -> list[index] = ingredient }
            }
        }

        val output = json.getAsJsonPrimitive("result").asString.let { itemId ->
            val item = Registry.ITEM.getOrEmpty(Identifier(itemId))
            return@let if (item.isPresent) {
                ItemStack(item.get())
            } else ItemStack.EMPTY
        }

        return TrialKeyAttuneRecipe(id, ingredients, output)
    }

    override fun read(id: Identifier, buf: PacketByteBuf): TrialKeyAttuneRecipe {
        val input = DefaultedList.ofSize(3, Ingredient.EMPTY)
        0.until(buf.readInt()).forEach { index ->
            input[index] = Ingredient.fromPacket(buf)
        }

        return TrialKeyAttuneRecipe(id, input, buf.readItemStack() ?: ItemStack.EMPTY)
    }
}
