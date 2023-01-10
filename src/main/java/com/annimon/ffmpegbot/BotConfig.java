package com.annimon.ffmpegbot;

import java.util.Set;

public record BotConfig(String botToken, String botUsername,
                        Set<Long> superUsers, Set<Long> allowedUsers) {

    boolean isUserAllowed(Long userId) {
        return superUsers().contains(userId)
                || allowedUsers().contains(userId);
    }
}
