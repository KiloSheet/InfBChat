package me.kilosheet.infbchat.utils;

import me.kilosheet.infbchat.InfBChat;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatListener implements Listener {

    private final InfBChat plugin;
    private final AntiSpam antiSpam;

    public ChatListener(InfBChat plugin) {
        this.plugin = plugin;
        this.antiSpam = new AntiSpam();
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if (player.hasPermission("infbchat.bypass.antispam")) {
            return;
        }

        long cooldown = plugin.getConfig().getInt("AntiSpam.chat_cooldown", 5);
        long remainingCooldown = antiSpam.getRemainingCooldown(player, cooldown);

        if (remainingCooldown > 0) {
            String message = plugin.getMessage("chat_spam_warning")
                    .replace("{seconds}", String.valueOf(remainingCooldown));
            player.sendMessage(new TextComponent(message));
            event.setCancelled(true);
        }
    }
}
