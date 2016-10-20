/**
 * 
 */
package com.rayzr522.minecraftmarkdown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Rayzr
 *
 */
public class ChatListener implements Listener {

    private MinecraftMarkdown plugin;

    public ChatListener(MinecraftMarkdown plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        String message = e.getMessage();
        for (Formatter formatter : plugin.getFormatters()) {
            message = formatter.apply(message);
        }
        e.setMessage(message);
    }

}
