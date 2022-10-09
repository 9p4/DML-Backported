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

package net.ersei.dml

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.mojang.logging.LogUtils
import com.sun.jna.StringArray
import net.ersei.dml.block.registerBlocks
import net.ersei.dml.blockEntity.registerBlockEntityTypes
import net.ersei.dml.entity.registerEntityRenderer
import net.ersei.dml.entity.registerEntityTypes
import net.ersei.dml.event.VanillaEvents
import net.ersei.dml.item.registerItems
import net.ersei.dml.listener.CrushingRecipeListener
import net.ersei.dml.listener.DataCollectListener
import net.ersei.dml.recipe.registerRecipeSerializers
import net.ersei.dml.recipe.registerRecipeTypes
import net.ersei.dml.screen.handler.registerScreenHandlers
import net.ersei.dml.screen.registerScreens
import net.ersei.dml.trial.TrialGriefPrevention
import net.ersei.dml.trial.affix.core.TrialAffixRegistry
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import org.checkerframework.checker.units.qual.min
import org.slf4j.LoggerFactory
import java.io.File
import java.io.PrintWriter
import java.nio.file.Files
import kotlin.random.Random
import kotlin.random.nextInt

const val MOD_ID = "dml-backported"

val LOGGER = LoggerFactory.getLogger(MOD_ID)

val config: ModConfig by lazy {
    val parser = JsonParser()
    val gson = GsonBuilder().setPrettyPrinting().create()
    val configFile = File("${FabricLoader.getInstance().configDir}${File.separator}$MOD_ID.json")
    var finalConfig: ModConfig
    LOGGER.info("Trying to read config file...")
    try {
        if (configFile.createNewFile()) {
            LOGGER.info("No config file found, creating a new one...")
            val json: String = gson.toJson(parser.parse(gson.toJson(ModConfig())))
            PrintWriter(configFile).use { out -> out.println(json) }
            finalConfig = ModConfig()
            LOGGER.info("Successfully created default config file.")
        } else {
            LOGGER.info("A config file was found, loading it..")
            finalConfig = gson.fromJson(String(Files.readAllBytes(configFile.toPath())), ModConfig::class.java)
            if (finalConfig == null) {
                throw NullPointerException("The config file was empty.")
            } else {
                LOGGER.info("Successfully loaded config file.")
            }
        }
    } catch (exception: Exception) {
        LOGGER.error("There was an error creating/loading the config file!", exception)
        finalConfig = ModConfig()
        LOGGER.warn("Defaulting to original config.")
    }
    finalConfig
}

@Suppress("unused")
fun init() {
    registerItems()
    registerBlocks()
    registerBlockEntityTypes()
    registerRecipeSerializers()
    registerRecipeTypes()
    registerScreenHandlers()
    registerEntityTypes()
    ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(DataCollectListener())
    AttackBlockCallback.EVENT.register(CrushingRecipeListener())
    TrialGriefPrevention().apply {
        AttackBlockCallback.EVENT.register(this)
        UseBlockCallback.EVENT.register(this)
        VanillaEvents.WorldExplosionEvent.register(this::explode)
        VanillaEvents.EndermanTeleportEvent.register(this::onEndermanTeleport)
    }
    TrialAffixRegistry.registerDefaultAffixes()
    LOGGER.info("Deep Mob Learning: Backported" + quirkyStartupMessages[Random.nextInt(quirkyStartupMessages.size)])
}

@Suppress("unused")
fun initClient() {
    registerScreens()
    registerEntityRenderer()
}

fun identifier(path: String) = Identifier(MOD_ID, path)

val quirkyStartupMessages = arrayOf(
    " is good to go",
    "'s body is ready!",
    " is starting up in... well, that depends on the other mods, really.",
    " will be challenging the System Glitch soon!",
    " had a good 8 hour sleep and is ready for the day.",
    " is warming up!"
)