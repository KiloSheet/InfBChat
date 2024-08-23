package me.kilosheet.infbchat.commands;

import me.kilosheet.infbchat.InfBChat;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NickCommand {

    private final InfBChat plugin;

    public NickCommand(InfBChat plugin) {
        this.plugin = plugin;
    }

    public void handleNickCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Only players can use this command."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (!player.hasPermission("infbchat.nick")) {
            player.sendMessage(new TextComponent(plugin.getMessage("no_permission")));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(new TextComponent(ChatColor.RED + "Usage: /ibc nick <nickname>"));
            return;
        }

        String nickname = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (!player.hasPermission("infbchat.nick.colorcodes")) {
            nickname = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', nickname));
        } else {
            nickname = ChatColor.translateAlternateColorCodes('&', nickname);
        }

        player.setDisplayName(nickname);
        player.setTabHeader(new TextComponent(nickname), null);

        player.sendMessage(new TextComponent(ChatColor.GREEN + "Your nickname has been changed to " + nickname));
    }
}
