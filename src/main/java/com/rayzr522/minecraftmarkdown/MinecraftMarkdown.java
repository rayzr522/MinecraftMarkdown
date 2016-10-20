package com.rayzr522.minecraftmarkdown;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftMarkdown extends JavaPlugin {

    private Logger                   logger;
    private List<Formatter>          formatters = new ArrayList<Formatter>();

    private static MinecraftMarkdown instance;

    @Override
    public void onEnable() {
        logger = getLogger();

        instance = this;

        addFormatter("_", "&o");
        addFormatter("*", "&l");
        addFormatter("`", "&7&o");

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        logger.info(versionText() + " enabled");
    }

    @Override
    public void onDisable() {
        logger.info(versionText() + " disabled");
    }

    public String versionText() {
        return getName() + " v" + getDescription().getVersion();
    }

    public static void addFormatter(String character, String colorCode) {
        getInstance().formatters.add(new Formatter(character, colorCode));
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

}
