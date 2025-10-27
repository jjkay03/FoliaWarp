package com.jjkay03.foliawarp.commands

import com.jjkay03.foliawarp.FoliaWarp
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class WarpCommand : CommandExecutor, TabCompleter {

    init {
        FoliaWarp.Companion.INSTANCE.getCommand("warp")?.setExecutor(this)
        FoliaWarp.Companion.INSTANCE.getCommand("warp")?.tabCompleter = this
    }

    // COMMAND
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // End if invalid arg
        if (args.isEmpty()) { sender.sendMessage("§cUsage: /$label <warp>"); return false }

        val warpName = args[0]
        val warpLocation = FoliaWarp.Companion.INSTANCE.getWarpLocation(warpName)

        // End if warp not found
        if (warpLocation == null) { sender.sendMessage("§cWarp not found!"); return false }

        // End if not player
        if (sender !is Player) { sender.sendMessage("§cOnly players can execute this command!"); return false }

        // Feedback
        sender.teleportAsync(warpLocation).thenAccept { _ ->
            sender.sendMessage("§aTeleported to warp: $warpName")
        }

        return true
    }

    // TAB COMPLETION
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String>? {
        if (args.size == 1) {
            return FoliaWarp.Companion.INSTANCE.getWarpNames().filter { it.lowercase().startsWith(args[0].lowercase()) }
        }
        return emptyList()
    }

}
