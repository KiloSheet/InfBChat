package me.kilosheet.infbchat.utils;

import me.kilosheet.infbchat.InfBChat;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerJoinListener implements Listener {

    private final InfBChat plugin;

    public PlayerJoinListener(InfBChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (plugin.isMOTDEnabled()) {
            String motdServer = plugin.getConfig().getString("MOTD.server");
            if (motdServer != null && !motdServer.isEmpty()) {
                if (player.getServer().getInfo().getName().equals(motdServer)) {
                    String motdMessage = plugin.getMOTDMessage();
                    if (motdMessage != null && !motdMessage.isEmpty()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', motdMessage));
                    }
                }
            } else {
                plugin.getLogger().warning("MOTD server is not configured correctly.");

            }
        }
    }
}
