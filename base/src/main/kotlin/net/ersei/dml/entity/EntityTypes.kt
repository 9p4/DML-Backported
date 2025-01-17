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

package net.ersei.dml.entity

import net.ersei.dml.entity.renderer.SystemGlitchRenderer
import net.ersei.dml.identifier
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

lateinit var SYSTEM_GLITCH_ENTITY_TYPE: EntityType<SystemGlitchEntity>

private fun registerAttributes() {
    FabricDefaultAttributeRegistry.register(SYSTEM_GLITCH_ENTITY_TYPE, SystemGlitchEntity.createMobAttributes())
}

fun registerEntityTypes() {
    SYSTEM_GLITCH_ENTITY_TYPE = Registry.register(
        Registry.ENTITY_TYPE,
        identifier("system_glitch"),
        FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, ::SystemGlitchEntity)
            .dimensions(EntityDimensions(1F, 2F, true))
            .build()
    )

    registerAttributes()
}

fun registerEntityRenderer() {
    EntityRendererRegistry.register(SYSTEM_GLITCH_ENTITY_TYPE) { dispatcher ->
        SystemGlitchRenderer(dispatcher, EntityModelLayers.ZOMBIE)
    }
}
