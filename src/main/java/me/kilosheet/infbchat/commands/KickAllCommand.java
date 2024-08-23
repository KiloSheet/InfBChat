package me.kilosheet.infbchat.commands;

import me.kilosheet.infbchat.InfBChat;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class KickAllCommand extends Command {

    private final InfBChat plugin;
    private static final String KICKALL_PERMISSION = "infbchat.kickall";
    private static final String KICK_REASON = "Kicked By Operator";

    public KickAllCommand(InfBChat plugin) {
        super("kickall", KICKALL_PERMISSION);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(KICKALL_PERMISSION)) {
            sender.sendMessage(new TextComponent(plugin.getMessage("no_permission")));
            return;
        }

        // Kick all players
        ProxyServer.getInstance().getPlayers().forEach(player -> player.disconnect(new TextComponent(KICK_REASON)));

        // Notify the sender
        sender.sendMessage(new TextComponent("All players have been kicked with reason: " + KICK_REASON));
    }
}
