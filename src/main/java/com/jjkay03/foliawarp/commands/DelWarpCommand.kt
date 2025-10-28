package com.jjkay03.foliawarp.commands

import com.jjkay03.foliawarp.FoliaWarp
import com.jjkay03.foliawarp.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class DelWarpCommand : CommandExecutor, TabCompleter {

    init {
        FoliaWarp.INSTANCE.getCommand("delwarp")?.setExecutor(this)
        FoliaWarp.INSTANCE.getCommand("delwarp")?.tabCompleter = this
    }

    // COMMAND
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // End if invalid arg
        if (args.isEmpty()) { sender.sendMessage("§cUsage: /$label <warp>"); return false }

        val warpName = args[0]

        synchronized(FoliaWarp.INSTANCE) {
            FoliaWarp.INSTANCE.config.set("warps.$warpName", null)
        }

        Utils.scheduleConfigSave()

        // Feedback
        sender.sendMessage("§3\uD83C\uDF00 §bDeleted warp §3$warpName")

        return true
    }

    // TAB COMPLETION
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String>? {
        if (args.size == 1) {
            return Utils.getWarpNames().filter { it.lowercase().startsWith(args[0].lowercase()) }
        }
        return emptyList()
    }

}
