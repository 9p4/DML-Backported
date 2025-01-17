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

package net.ersei.dml.modular_armor.hud

import net.ersei.dml.identifier
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack

class UndyingCooldownHud : DrawableHelper() {

    companion object {
        val INSTANCE = UndyingCooldownHud()
        private val TEXTURE = identifier("textures/gui/undying_cooldown.png")
    }

    private val client = MinecraftClient.getInstance()

    var cooldownTime = 0
    var maxCooldownTime = 0

    fun render(matrices: MatrixStack) {
        if (cooldownTime == 0 || maxCooldownTime == 0) {
            return
        }

        // TODO render a progress bar or something
        // val percent = cooldownTime / maxCooldownTime
        val width = client.window.scaledWidth
        val height = client.window.scaledHeight

        matrices.push()
        client.textureManager.bindTexture(TEXTURE)
        drawTexture(
            matrices,
            width - 64,
            height - 18,
            0F, 0F,
            16, 16,
            16, 16
        )
        matrices.pop()

    }
}
