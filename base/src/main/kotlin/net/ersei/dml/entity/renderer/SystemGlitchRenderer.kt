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

package net.ersei.dml.entity.renderer

import net.ersei.dml.entity.SystemGlitchEntity
import net.ersei.dml.entity.model.SystemGlitchModel
import net.ersei.dml.identifier
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer
import net.minecraft.client.render.entity.model.EntityModelLayer

class SystemGlitchRenderer(context: EntityRendererFactory.Context, layer: EntityModelLayer) :
    MobEntityRenderer<SystemGlitchEntity, SystemGlitchModel>(
        context, SystemGlitchModel(context.getPart(layer)), 0.5F
    )
{

    override fun getTexture(entity: SystemGlitchEntity) = identifier("textures/entity/system_glitch.png")

    init {
        addFeature(HeadFeatureRenderer(this, context.modelLoader, 1.15F, 1.05F, 1.15F))
    }
}
