package com.github.puregero.warpplugin;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class WarpPluginPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();

        new WarpCommand(this);
        new SetWarpCommand(this);
        new DelWarpCommand(this);
    }

    public Collection<String> getWarpNames() {
        ConfigurationSection warpsConfig = this.getConfig().getConfigurationSection("warps");
        if (warpsConfig == null) {
            return List.of();
        }
        return warpsConfig.getKeys(false);
    }

    public @Nullable Location getWarpLocation(String name) {
        ConfigurationSection warpsConfig = this.getConfig().getConfigurationSection("warps");
        if (warpsConfig == null || !warpsConfig.contains(name)) {
            return null;
        }
        ConfigurationSection warpSection = warpsConfig.getConfigurationSection(name);
        if (warpSection == null) {
            return null;
        }
        String world = warpSection.getString("world");
        double x = warpSection.getDouble("x");
        double y = warpSection.getDouble("y");
        double z = warpSection.getDouble("z");
        float yaw = (float) warpSection.getDouble("yaw", 0);
        float pitch = (float) warpSection.getDouble("pitch", 0);
        if (world == null) {
            return null;
        }
        return new Location(this.getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    public void scheduleConfigSave() {
        this.getServer().getAsyncScheduler().runNow(this, task -> {
            synchronized (this) {
                this.saveConfig();
            }
        });
    }

}
