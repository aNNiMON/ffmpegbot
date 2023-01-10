package com.annimon.ffmpegbot;

import java.util.Set;

public record BotConfig(String botToken, String botUsername, Set<Long> allowedUsers) {

    boolean isUserAllowed(Long userId) {
        return allowedUsers().contains(userId);
    }
}
