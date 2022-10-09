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

package net.ersei.dml.modular_armor.net.consumers

import net.ersei.dml.modular_armor.event.ModularArmorEvents
import net.ersei.dml.utils.readVec3d
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity

class TeleportEffectRequestedPacketConsumer : ServerPlayNetworking.PlayChannelHandler {

    override fun receive(
        server: MinecraftServer,
        player: ServerPlayerEntity,
        handler: ServerPlayNetworkHandler,
        buf: PacketByteBuf,
        responseSender: PacketSender
    ) {
        val pos = buf.readVec3d()
        val rotation = buf.readVec3d()
        server.execute {
            if (
                pos.squaredDistanceTo(player.pos) <= 4*4
                && arrayOf(rotation.x, rotation.y, rotation.z).all { it in -128F..128F }
            ) {
                ModularArmorEvents.TeleportEffectRequestedEvent.invoker().invoke(player, pos, rotation)
            }
        }
    }
}
