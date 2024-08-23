package me.kilosheet.infbchat.commands;

import me.kilosheet.infbchat.InfBChat;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class MsgCommand extends Command implements TabExecutor {

    private final InfBChat plugin;

    public MsgCommand(InfBChat plugin) {
        super("msg");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(new TextComponent(plugin.getMessage("msg_usage")));
            return;
        }

        String targetPlayerName = args[0];
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }
        String message = messageBuilder.toString();


        ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetPlayerName);

        if (targetPlayer == null) {
            sender.sendMessage(new TextComponent(plugin.getMessage("msg_player_not_found")));
            return;
        }

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer senderPlayer = (ProxiedPlayer) sender;
            String senderMessage = plugin.getMessage("msg_sender_format")
                    .replace("{player}", targetPlayer.getName())
                    .replace("{message}", message);
            senderPlayer.sendMessage(new TextComponent(senderMessage));
        }

        String receiverMessage = plugin.getMessage("msg_receiver_format")
                .replace("{player}", sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getName() : sender.getName())
                .replace("{message}", message);
        targetPlayer.sendMessage(new TextComponent(receiverMessage));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return new ArrayList<>();
    }

}
