package com.github.puregero.warpplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class DelWarpCommand implements CommandExecutor, TabCompleter {
    private final WarpPluginPlugin plugin;

    public DelWarpCommand(WarpPluginPlugin plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("delwarp")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("delwarp")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /%s <warp>".formatted(label)).color(NamedTextColor.RED));
            return false;
        }

        String warpName = args[0];

        synchronized (this.plugin) {
            this.plugin.getConfig().set("warps." + warpName, null);
        }

        this.plugin.scheduleConfigSave();

        sender.sendMessage(Component.text("Warp deleted: " + warpName).color(NamedTextColor.GREEN));

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
