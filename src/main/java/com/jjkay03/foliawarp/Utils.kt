package com.jjkay03.foliawarp

import org.bukkit.Location

object Utils {

    // Function to get all warp names from config
    fun getWarpNames(): Collection<String> {
        val warpsConfig = FoliaWarp.INSTANCE.config.getConfigurationSection("warps") ?: return emptyList()
        return warpsConfig.getKeys(false)
    }

    // Function to get warp location from config by name
    fun getWarpLocation(name: String): Location? {
        val warpsConfig = FoliaWarp.INSTANCE.config.getConfigurationSection("warps") ?: return null
        if (!warpsConfig.contains(name)) return null

        val warpSection = warpsConfig.getConfigurationSection(name) ?: return null

        val world = warpSection.getString("world") ?: return null
        val x = warpSection.getDouble("x")
        val y = warpSection.getDouble("y")
        val z = warpSection.getDouble("z")
        val yaw = warpSection.getDouble("yaw", 0.0).toFloat()
        val pitch = warpSection.getDouble("pitch", 0.0).toFloat()

        return Location(FoliaWarp.INSTANCE.server.getWorld(world), x, y, z, yaw, pitch)
    }

    // Function to schedule async config save
    fun scheduleConfigSave() {
        FoliaWarp.INSTANCE.server.asyncScheduler.runNow(FoliaWarp.INSTANCE) { _ ->
            synchronized(this) {
                FoliaWarp.INSTANCE.saveConfig()
            }
        }
    }

}