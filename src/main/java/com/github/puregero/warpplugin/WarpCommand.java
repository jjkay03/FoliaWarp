package com.github.puregero.warpplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class WarpCommand implements CommandExecutor, TabCompleter {
    private final WarpPluginPlugin plugin;

    public WarpCommand(WarpPluginPlugin plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("warp")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("warp")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /%s <warp>".formatted(label)).color(NamedTextColor.RED));
            return false;
        }

        String warpName = args[0];
        Location warpLocation = this.plugin.getWarpLocation(warpName);

        if (warpLocation == null) {
            sender.sendMessage(Component.text("Warp not found: " + warpName).color(NamedTextColor.RED));
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can execute this command!").color(NamedTextColor.RED));
            return false;
        }

        player.sendMessage(Component.text("Teleporting...").color(NamedTextColor.GREEN));
        player.teleportAsync(warpLocation).thenAccept(teleported ->
            player.sendMessage(Component.text("Teleported to warp: " + warpName).color(NamedTextColor.GREEN))
        );

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return this.plugin.getWarpNames().stream().filter(warpName -> warpName.toLowerCase().startsWith(args[0].toLowerCase())).toList();
        }
        return List.of();
    }
}
