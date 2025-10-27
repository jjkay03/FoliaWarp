package com.jjkay03.foliawarp

import com.jjkay03.foliawarp.commands.*
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class FoliaWarp : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: FoliaWarp
    }

    // PLUGIN STARTUP LOGIC
    override fun onEnable() {
        INSTANCE = this

        // CONFIGURATION
        saveDefaultConfig()  // Save default config
        reloadConfig()       // Reload default config

        // REGISTER COMMANDS
        WarpCommand()
        SetWarpCommand()
        DelWarpCommand()
    }

    // Get all warp names from config
    fun getWarpNames(): Collection<String> {
        val warpsConfig = config.getConfigurationSection("warps") ?: return emptyList()
        return warpsConfig.getKeys(false)
    }

    // Get warp location from config by name
    fun getWarpLocation(name: String): Location? {
        val warpsConfig = config.getConfigurationSection("warps") ?: return null
        if (!warpsConfig.contains(name)) return null

        val warpSection = warpsConfig.getConfigurationSection(name) ?: return null

        val world = warpSection.getString("world") ?: return null
        val x = warpSection.getDouble("x")
        val y = warpSection.getDouble("y")
        val z = warpSection.getDouble("z")
        val yaw = warpSection.getDouble("yaw", 0.0).toFloat()
        val pitch = warpSection.getDouble("pitch", 0.0).toFloat()

        return Location(server.getWorld(world), x, y, z, yaw, pitch)
    }

    // Schedule async config save
    fun scheduleConfigSave() {
        server.asyncScheduler.runNow(this) { _ ->
            synchronized(this) {
                saveConfig()
            }
        }
    }

}
