package me.kilosheet.infbchat.utils;

import java.util.concurrent.ConcurrentHashMap;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AntiSpam {

    private final ConcurrentHashMap<ProxiedPlayer, Long> cooldowns = new ConcurrentHashMap<>();

    public long getRemainingCooldown(ProxiedPlayer player, long cooldownInSeconds) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = cooldownInSeconds * 1000;

        if (cooldowns.containsKey(player)) {
            long lastMessageTime = cooldowns.get(player);
            long remainingTime = (cooldownTime - (currentTime - lastMessageTime)) / 1000;
            if (remainingTime > 0) {
                return remainingTime;
            }
        }

        cooldowns.put(player, currentTime);
        return 0;
    }

    public boolean canSendMessage(ProxiedPlayer player, long cooldownInSeconds) {
        return getRemainingCooldown(player, cooldownInSeconds) == 0;
    }


}
