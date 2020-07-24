/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
 */

package dev.nathanpb.dml.entity

import dev.nathanpb.dml.data.DataModelTier
import dev.nathanpb.dml.entity.goal.GlitchTeleportTowardsPlayerGoal
import dev.nathanpb.dml.trial.TrialGriefPrevention
import dev.nathanpb.dml.utils.randomAround
import dev.nathanpb.dml.utils.runningTrials
import dev.nathanpb.dml.utils.toVec3d
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class SystemGlitchEntity(type: EntityType<out HostileEntity>, world: World) : HostileEntity(type, world) {
    companion object {
        fun createMobAttributes(): DefaultAttributeContainer.Builder = LivingEntity.createLivingAttributes()
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 4.0)
            .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0)
    }

    /**
     * This property is used to set the glitch's max health in the Trial's start time
     * Because at that time the entity isn't bound to the trial yet
     * And the lazy property [trial] will be null at that time, causing it to have null value forever
     *
     * If its value is null, the entity is not attuned to a trial yet (in case of reading in the entity spawn time)
     * Or it just doesn't belong to a trial (in case of reading when a trial is already started)
     */
    var tier: DataModelTier? = null

    /**
     * Find's the trial that this entity belongs to
     *
     * PLEASE do not call this during the trial initialization, this will ruin this entity's instance
     * causing its trial to be null until the game reload
     */
    val trial by lazy {
        world.runningTrials.firstOrNull {
            it.systemGlitch == this
        }
    }

    override fun initGoals() {
        super.initGoals()
        goalSelector.add(1, WanderAroundGoal(this, 1.0))
        goalSelector.add(1, LookAroundGoal(this))
        goalSelector.add(2, MeleeAttackGoal(this, 1.0, true))

        targetSelector.add(1, GlitchTeleportTowardsPlayerGoal(this))
        targetSelector.add(2, FollowTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.add(3, RevengeGoal(this))

        // rage attacks
        // ranged attack
    }

    /**
     * Teleports the entity in a random block of the radius of [radius] around [at]
     *
     * If the entity belongs to a trial, the entity will not be teleported to blocks outside its trial area
     * In that case, the entity will attempt to teleport maximum [maxAttempts] times checking if the random picked block is inside its [trial] area
     * And successfully teleports if it does
     *
     * @param at The initial block to find random ones around
     * @param radius The radius to find random blocks around
     * @param maxAttempts The maximum attempts to teleport the entity, in case of trying to teleport outside trials
     * @returns True if the entity was teleported, false if the max teleport attempts was reached and the entity was not teleported
     */
    fun tryTeleportRandomly(at: BlockPos, radius: Int, maxAttempts: Int = 5): Boolean {
        var teleportsAttempt = 0
        do {
            val pos = at.randomAround(radius, 0, radius)
            val canTeleport = trial?.let { trial ->
                TrialGriefPrevention.isBlockProtected(pos, trial)
            } ?: true

            if (canTeleport) {
                pos.toVec3d().apply {
                    teleport(x, y, z)
                    return true
                }
            }
        } while (teleportsAttempt++ < maxAttempts)
        return false
    }
}