package com.github.puregero.warpplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetWarpCommand implements CommandExecutor {
    private final WarpPluginPlugin plugin;

    public SetWarpCommand(WarpPluginPlugin plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("setwarp")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /%s <warp>".formatted(label)).color(NamedTextColor.RED));
            return false;
        }

        String warpName = args[0];

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can execute this command!").color(NamedTextColor.RED));
            return false;
        }

        Location location = player.getLocation();

        synchronized (this.plugin) {
            ConfigurationSection warpSection = this.plugin.getConfig().createSection("warps." + warpName);
            warpSection.set("world", location.getWorld().getName());
            warpSection.set("x", location.getBlockX() + 0.5);
            warpSection.set("y", location.getY());
            warpSection.set("z", location.getBlockZ() + 0.5);
            warpSection.set("yaw", (int) ((location.getYaw() + 45) / 90) * 90); // round to nearest 90 degrees
            warpSection.set("pitch", 0);
        }

        this.plugin.scheduleConfigSave();

        player.sendMessage(Component.text("Warp set: " + warpName).color(NamedTextColor.GREEN));

        return true;
    }
}
