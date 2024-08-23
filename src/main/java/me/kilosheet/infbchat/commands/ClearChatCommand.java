package me.kilosheet.infbchat.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.ProxyServer;
import me.kilosheet.infbchat.InfBChat;

public class ClearChatCommand {

    private final InfBChat plugin;
    private static final String CLEAR_CHAT_PERMISSION = "infbchat.clearchat";

    public ClearChatCommand(InfBChat plugin) {
        this.plugin = plugin;
    }

    public void clearChat(CommandSender sender) {
        if (!sender.hasPermission(CLEAR_CHAT_PERMISSION)) {
            sender.sendMessage(new TextComponent(plugin.getMessage("no_permission")));
            return;
        }

        ProxiedPlayer[] players = ProxyServer.getInstance().getPlayers().toArray(new ProxiedPlayer[0]);
        TextComponent emptyMessage = new TextComponent("");
        for (ProxiedPlayer player : players) {
            for (int i = 0; i < 100; i++) {
                player.sendMessage(emptyMessage);
            }
        }
        sender.sendMessage(new TextComponent(plugin.getMessage("clear_chat_message")));
    }
}
