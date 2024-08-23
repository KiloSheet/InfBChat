package me.kilosheet.infbchat;

import me.kilosheet.infbchat.commands.IbcCommand;
import me.kilosheet.infbchat.commands.MsgCommand;
import me.kilosheet.infbchat.commands.KickAllCommand;
import me.kilosheet.infbchat.utils.ChatListener;
import me.kilosheet.infbchat.utils.PlayerJoinListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.api.ProxyServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class InfBChat extends Plugin {

    private Configuration config;
    private boolean motdEnabled;
    private String motdMessage;
    private Configuration messages;

    @Override
    public void onEnable() {
        if (!loadConfig() || !validateServers()) {
            getLogger().severe(getMessage("glist_invalid_server"));
            return;
        }

        loadConfig();



        loadMessages();

        getProxy().getPluginManager().registerCommand(this, new MsgCommand(this));
        getProxy().getPluginManager().registerCommand(this, new IbcCommand(this));
        getProxy().getPluginManager().registerCommand(this, new KickAllCommand(this));

        getProxy().getPluginManager().registerListener(this, new ChatListener(this));

        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener(this));


        String asciiArt = ChatColor.AQUA +
                "\n /$$$$$$            /$$$$$$  /$$$$$$$   /$$$$$$  /$$                   /$$\n" +
                "  |_  $$_/           /$$__  $$| $$__  $$ /$$__  $$| $$                  | $$\n" +
                "    | $$   /$$$$$$$ | $$  \\__/| $$  \\ $$| $$  \\__/| $$$$$$$   /$$$$$$  /$$$$$$\n" +
                "    | $$  | $$__  $$| $$$$    | $$$$$$$ | $$      | $$__  $$ |____  $$|_  $$_/\n" +
                "    | $$  | $$  \\ $$| $$_/    | $$__  $$| $$      | $$  \\ $$  /$$$$$$$  | $$\n" +
                "    | $$  | $$  | $$| $$      | $$  \\ $$| $$    $$| $$  | $$ /$$__  $$  | $$ /$$\n" +
                "    /$$$$$$| $$  | $$| $$      | $$$$$$$/|  $$$$$$/| $$  | $$|  $$$$$$$  |  $$$$/\n" +
                "   |______/|__/  |__/|__/      |_______/  \\______/ |__/  |__/ \\_______/   \\___/\n" +
                ChatColor.BLUE + ChatColor.STRIKETHROUGH +
                "                      =>InfinityBungeeChat<=" + ChatColor.RESET;

        getLogger().info(asciiArt);

    }

    private boolean loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                getDataFolder().mkdir();
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!config.contains("AntiSpam.chat_cooldown")) {
            config.set("AntiSpam.chat_cooldown", 5);
            saveConfig();
        }

        if (!config.contains("MOTD")) {
            config.set("MOTD.enabled", false);
            config.set("MOTD.message", "");
            config.set("MOTD.server", "");

            saveConfig();
        }

        return true;
    }
    private void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean loadMessages() {
        File messagesFile = new File(getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            try (InputStream in = getResourceAsStream("messages.yml")) {
                Files.copy(in, messagesFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(messagesFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void loadMOTD() {
        motdEnabled = config.getBoolean("MOTD.enabled", false);
        motdMessage = config.getString("MOTD.message", "");
    }

    public boolean isMOTDEnabled() {
        return motdEnabled;
    }

    public String getMOTDMessage() {
        return motdMessage;
    }



    public void reloadConfig() {
        loadConfig();
        loadMessages();
    }

    private boolean validateServers() {
        Configuration config = getConfig();
        if (config.contains("servers")) {
            for (String server : config.getSection("servers").getKeys()) {
                if (ProxyServer.getInstance().getServerInfo(server) == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String getMessage(String key) {
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', messages.getString("messages." + key));
    }

    public Configuration getMessages() {
        return messages;
    }

    public Configuration getConfig() {
        return config;
    }

    private void saveResource(String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in plugin jar");
        }

        File outFile = new File(getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                try (OutputStream out = new FileOutputStream(outFile)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
