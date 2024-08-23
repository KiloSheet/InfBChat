package me.kilosheet.infbchat.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.api.ProxyServer;
import me.kilosheet.infbchat.InfBChat;

public class GlistCommand extends Command {

    private final InfBChat plugin;

    public GlistCommand(InfBChat plugin) {
        super("glist");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendGlist(sender);
    }

    public void sendGlist(CommandSender sender) {
        int totalPlayers = ProxyServer.getInstance().getOnlineCount();
        String header = plugin.getMessage("glist_header").replace("{PLAYERS}", String.valueOf(totalPlayers));
        sender.sendMessage(new TextComponent(header));

        Configuration config = plugin.getConfig();

        for (String server : config.getSection("servers").getKeys()) {
            int playerCount = ProxyServer.getInstance().getServerInfo(server).getPlayers().size();
            int maxPlayers = config.getInt("servers." + server + ".max-players");
            String progressBar = createProgressBar(playerCount, maxPlayers, 50, '|');

            String line = plugin.getMessage("glist_line")
                    .replace("{SERVER_NAME}", server)
                    .replace("{PROGRESS_BAR}", progressBar)
                    .replace("{CURRENT_PLAYERS}", String.valueOf(playerCount))
                    .replace("{MAX_PLAYERS}", String.valueOf(maxPlayers));

            sender.sendMessage(new TextComponent(line));
        }
    }

    private String createProgressBar(int current, int max, int totalBars, char symbol) {
        float percent = max > 0 ? (float) current / max : 0;
        int bars = Math.round(percent * totalBars);
        StringBuilder bar = new StringBuilder();

        bar.append("§a");
        for (int i = 0; i < bars; i++) {
            bar.append(symbol);
        }
        bar.append("§7");
        for (int i = bars; i < totalBars; i++) {
            bar.append(symbol);
        }

        return bar.toString();
    }
}
