package com.jjkay03.foliawarp.commands

import com.jjkay03.foliawarp.FoliaWarp
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class WarpCommand : CommandExecutor, TabCompleter {

    init {
        FoliaWarp.INSTANCE.getCommand("warp")?.setExecutor(this)
        FoliaWarp.INSTANCE.getCommand("warp")?.tabCompleter = this
    }

    // COMMAND
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // End if invalid arg
        if (args.isEmpty()) { sender.sendMessage("§cUsage: /$label <warp> [player]"); return false }

        val warpName = args[0]
        val warpLocation = FoliaWarp.INSTANCE.getWarpLocation(warpName)

        // End if warp not found
        if (warpLocation == null) { sender.sendMessage("§cWarp not found!"); return false }

        // Check if player argument provided
        if (args.size >= 2) {
            val targetPlayerName = args[1]
            val targetPlayer = Bukkit.getPlayer(targetPlayerName)

            // End if target player not found
            if (targetPlayer == null) { sender.sendMessage("§cPlayer not found!"); return false }

            // Teleport target player
            targetPlayer.teleportAsync(warpLocation).thenAccept { _ ->
                targetPlayer.sendMessage("§3\uD83C\uDF00 §bYou have been warped to §3$warpName")
                sender.sendMessage("§3\uD83C\uDF00 §bWarped §r${targetPlayer.name}§b to §3$warpName")
            }
        } else {
            // End if not player
            if (sender !is Player) { sender.sendMessage("§cOnly players can execute this command!"); return false }

            // Teleport sender
            sender.teleportAsync(warpLocation).thenAccept { _ ->
                sender.sendMessage("§3\uD83C\uDF00 §bWarped to §3$warpName")
            }
        }

        return true
    }

    // TAB COMPLETION
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String>? {
        if (args.size == 1) {
            return FoliaWarp.INSTANCE.getWarpNames().filter { it.lowercase().startsWith(args[0].lowercase()) }
        }
        if (args.size == 2) {
            return Bukkit.getOnlinePlayers().map { it.name }.filter { it.lowercase().startsWith(args[1].lowercase()) }
        }
        return emptyList()
    }

}
