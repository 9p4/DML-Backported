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

package net.ersei.dml.modular_armor.effects

import net.ersei.dml.config
import net.ersei.dml.enums.DataModelTier
import net.ersei.dml.enums.EntityCategory
import net.ersei.dml.event.VanillaEvents
import net.ersei.dml.identifier
import net.ersei.dml.modular_armor.core.*
import net.ersei.dml.modular_armor.net.S2C_UNDYING_COOLDOWN_UPDATE
import net.ersei.dml.modular_armor.undyingLastUsage
import net.ersei.dml.utils.`if`
import net.ersei.dml.utils.firstInstanceOrNull
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import kotlin.math.max

class UndyingEffect : ModularEffect<ModularEffectTriggerPayload>(
    identifier("undying"),
    EntityCategory.ILLAGER,
    config.glitchArmor.costs::undying
) {

    companion object {
        private val INSTANCE by lazy {
            ModularEffectRegistry.INSTANCE.all.firstInstanceOrNull<UndyingEffect>()
        }

        fun trigger(player: PlayerEntity) = INSTANCE?.run {
            return@run ModularEffectContext.from(player)
                .run(EffectStackOption.RANDOMIZE.apply)
                .any { attemptToApply(it, ModularEffectTriggerPayload.EMPTY) == ActionResult.SUCCESS }
                .`if` {
                    player.undyingLastUsage = player.world.time
                    player.health = 1.0f
                    player.clearStatusEffects()
                    player.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 900, 1))
                    player.addStatusEffect(StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1))
                    player.addStatusEffect(StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0))
                    player.world.sendEntityStatus(player, 35.toByte())

                    if (player is ServerPlayerEntity) {
                        player.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING))
                        Criteria.USED_TOTEM.trigger(player, ItemStack(Items.TOTEM_OF_UNDYING))
                    }
                    syncCooldown(player, config.glitchArmor.undyingCooldownTime)
                    true
                }
        } ?: false
    }

    override fun registerEvents() {
        VanillaEvents.PlayerEntityTickEvent.register { player ->
            if (!player.world.isClient && player.world.time % 40 == 0L) {
                ModularEffectContext.from(player)
                    .any { super.canApply(it, ModularEffectTriggerPayload.EMPTY) }
                    .let {
                       syncCooldown(player, if (it) config.glitchArmor.undyingCooldownTime else 0)
                    }
            }
        }

    }

    override fun acceptTier(tier: DataModelTier) = tier.isMaxTier()

    private fun remainingCooldownTime(player: PlayerEntity): Int = player.undyingLastUsage?.let {
        max(config.glitchArmor.undyingCooldownTime - (player.world.time - it), 0).toInt()
    } ?: 0

    private fun isUnderCooldown(player: PlayerEntity) : Boolean {
        return remainingCooldownTime(player) > 0
    }

    override fun canApply(context: ModularEffectContext, payload: ModularEffectTriggerPayload): Boolean {
        return super.canApply(context, payload) && !isUnderCooldown(context.player)
    }

    fun syncCooldown(player: PlayerEntity, maxCooldownTime: Int) {
        if (player is ServerPlayerEntity) {
            val packet = PacketByteBuf(Unpooled.buffer()).apply {
                writeInt(remainingCooldownTime(player))
                writeInt(maxCooldownTime)
            }
            ServerPlayNetworking.send(player, S2C_UNDYING_COOLDOWN_UPDATE, packet)
        }
    }
}
