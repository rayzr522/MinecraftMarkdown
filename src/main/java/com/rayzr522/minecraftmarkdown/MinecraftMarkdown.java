package com.rayzr522.minecraftmarkdown;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftMarkdown extends JavaPlugin {

    private static MinecraftMarkdown instance;

    private Logger                   logger;
    private List<Formatter>          formatters = new ArrayList<Formatter>();
    private boolean                  convertColorCodes;

    @Override
    public void onEnable() {
        logger = getLogger();

        instance = this;

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getCommand("minecraftmarkdown").setExecutor(this);

        load();

        logger.info(versionText() + " enabled");
    }

    @Override
    public void onDisable() {
        logger.info(versionText() + " disabled");
    }

    private void load() {

        File file = getFile("config.yml");
        if (!file.exists()) {
            saveResource("config.yml", true);
        }

        reloadConfig();
        formatters.clear();

        convertColorCodes = getConfig().getBoolean("convert-formatting-codes", false);
        ConfigurationSection formattersSection = getConfig().getConfigurationSection("formatters");
        for (String key : formattersSection.getKeys(false)) {
            ConfigurationSection cs = formattersSection.getConfigurationSection(key);
            Objects.requireNonNull(cs, "Formatter must be a valid configuration section!");
            formatters.add(new Formatter(cs));
        }

    }

    private File getFile(String path) {
        return new File(getDataFolder(), path);
    }

    public String versionText() {
        return getName() + " v" + getDescription().getVersion();
    }

    public static void addFormatter(String character, String start, String end) {
        getInstance().formatters.add(new Formatter(character, start, end));
    }

    public static void addFormatter(String character, String start) {
        getInstance().formatters.add(new Formatter(character, start));
    }

    public static MinecraftMarkdown getInstance() {
        return instance;
    }

    /**
     * @return
     */
    public List<Formatter> getFormatters() {
        return formatters;
    }

    /**
     * @return should we convert color and formatting codes?
     */
    public boolean convertColorCodes() {
        return convertColorCodes;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.GRAY + "This server is running " + ChatColor.AQUA + versionText());
            return true;
        }

        String arg = args[0].toLowerCase();
        if (arg.equals("reload")) {
            load();
            sender.sendMessage(ChatColor.GRAY + "Config reloaded for " + ChatColor.AQUA + getName());
        } else {
            return false;
        }

        return true;
    }

}
