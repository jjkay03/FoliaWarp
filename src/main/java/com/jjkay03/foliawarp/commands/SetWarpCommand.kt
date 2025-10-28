package com.jjkay03.foliawarp.commands

import com.jjkay03.foliawarp.FoliaWarp
import com.jjkay03.foliawarp.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetWarpCommand : CommandExecutor {

    init {
        FoliaWarp.INSTANCE.getCommand("setwarp")?.setExecutor(this)
    }

    // COMMAND
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // End if invalid arg
        if (args.isEmpty()) { sender.sendMessage("§cUsage: /$label <warp>"); return false }

        val warpName = args[0]

        // End if not player
        if (sender !is Player) { sender.sendMessage("§cOnly players can execute this command!"); return false }

        // Create warp
        val location = sender.location
        synchronized(FoliaWarp.INSTANCE) {
            val warpSection = FoliaWarp.INSTANCE.config.createSection("warps.$warpName")
            warpSection.set("world", location.world.name)
            warpSection.set("x", location.blockX + 0.5)
            warpSection.set("y", location.y)
            warpSection.set("z", location.blockZ + 0.5)
            warpSection.set("yaw", ((location.yaw + 45) / 90).toInt() * 90) // round to nearest 90 degrees
            warpSection.set("pitch", 0)
        }

        // Save warp in config
        Utils.scheduleConfigSave()

        // Feedback
        sender.sendMessage("§3\uD83C\uDF00 §bSet warp §3$warpName")

        return true
    }

}
