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

package net.ersei.dml.screen.handler.widget

import net.ersei.dml.screen.handler.DeepLearnerScreenHandler
import net.ersei.dml.utils.drawEntity
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.village.VillagerData
import net.minecraft.village.VillagerDataContainer
import net.minecraft.village.VillagerProfession
import net.minecraft.village.VillagerType
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class WEntityShowcase(
    private val root: SyncedGuiDescription
) : WWidget() {

    var entityTypes: List<EntityType<*>> = emptyList()

    private var tickCount = 0

    val entityType: EntityType<*>?
        get() {
            return entityTypes.takeIf { entityTypes.isNotEmpty() }?.let { values ->
                values.toTypedArray()[(tickCount / 60) % values.size]
            }
        }

    override fun tick() {
        super.tick()
        tickCount++
    }

    override fun canResize() = true

    override fun paint(matrices: MatrixStack, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        super.paint(matrices, x, y, mouseX, mouseY)
        matrices.push()

        val entityType = entityType ?: return
        val world = MinecraftClient.getInstance().world ?: return
        val entity = entityType.create(world) as? LivingEntity ?: return

        if (entity is VillagerDataContainer)
            entity.villagerData = VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 0)

        val w = (this.width / 2)
        val h = (height / 1.25).roundToInt()

        val scaleFactor = min(w, h) * 1.5
        val entityScaleFactor = max(entity.width, entity.height)

        drawEntity(
            entity,
            x + w,
            y + h,
            (scaleFactor / entityScaleFactor).roundToInt(),
            0, 180F,
            (tickCount % 360F) * 2F + 150F
        )
        matrices.pop()

        // only update at the exact tick the mob changes
        if(tickCount % 3 == 0) {
            (root as DeepLearnerScreenHandler).updateEntityInformation()
        }
    }

}
