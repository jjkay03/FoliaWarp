package com.jjkay03.foliawarp

import com.jjkay03.foliawarp.commands.*
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

}
