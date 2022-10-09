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

package net.ersei.dml.modular_armor.net

import net.ersei.dml.identifier
import net.ersei.dml.modular_armor.net.consumers.ModularEffectTogglePacketConsumer
import net.ersei.dml.modular_armor.net.consumers.SoulVisionRequestedPacketConsumer
import net.ersei.dml.modular_armor.net.consumers.TeleportEffectRequestedPacketConsumer
import net.ersei.dml.modular_armor.net.consumers.client.FlightBurnoutManagerUpdatePacketConsumer
import net.ersei.dml.modular_armor.net.consumers.client.UndyingCooldownUpdatePacketConsumer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

val C2S_TELEPORT_EFFECT_REQUESTED = identifier("teleport_effect_requested")
val C2S_SOUL_VISION_REQUESTED = identifier("soul_vision_requested")
val C2S_MODULAR_EFFECT_TOGGLE = identifier("modular_effect_toggle")

val S2C_FLIGHT_BURNOUT_MANAGER_UPDATE = identifier("flight_burnout_manager_update")
val S2C_UNDYING_COOLDOWN_UPDATE = identifier("undying_cooldown_update")

fun registerClientSidePackets() {
    hashMapOf(
        S2C_FLIGHT_BURNOUT_MANAGER_UPDATE to FlightBurnoutManagerUpdatePacketConsumer(),
        S2C_UNDYING_COOLDOWN_UPDATE to UndyingCooldownUpdatePacketConsumer()
    ).forEach { (id, consumer) ->
        ClientPlayNetworking.registerGlobalReceiver(id, consumer)
    }
}

fun registerServerSidePackets() {
    hashMapOf(
        C2S_TELEPORT_EFFECT_REQUESTED to TeleportEffectRequestedPacketConsumer(),
        C2S_SOUL_VISION_REQUESTED to SoulVisionRequestedPacketConsumer(),
        C2S_MODULAR_EFFECT_TOGGLE to ModularEffectTogglePacketConsumer()
    ).forEach { (id, consumer) ->
        ServerPlayNetworking.registerGlobalReceiver(id, consumer)
    }
}
