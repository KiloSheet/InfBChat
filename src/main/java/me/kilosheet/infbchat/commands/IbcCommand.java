package me.kilosheet.infbchat.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import me.kilosheet.infbchat.InfBChat;

public class IbcCommand extends Command {

    private final InfBChat plugin;

    public IbcCommand(InfBChat plugin) {
        super("ibc", null, "infbchat");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(new TextComponent(plugin.getMessage("help_message")));
        } else if (args[0].equalsIgnoreCase("clearchat")) {
            new ClearChatCommand(plugin).clearChat(sender);
        } else if (args[0].equalsIgnoreCase("glist")) {
            new GlistCommand(plugin).sendGlist(sender);

        } else if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("infbchat.reload")) {
                plugin.reloadConfig();
                sender.sendMessage(new TextComponent(plugin.getMessage("reload_message")));
            } else {
                sender.sendMessage(new TextComponent(plugin.getMessage("no_permission")));
            }
        } else if (args[0].equalsIgnoreCase("nick")) {
            new NickCommand(plugin).handleNickCommand(sender, args);
        } else {
            sender.sendMessage(new TextComponent("§cUnknown command. Use /ibc help for help."));
        }
    }
}
