package com.annimon.ffmpegbot;

import java.util.Set;

public record BotConfig(String appId, String appHash,
                        String botToken, String botUsername,
                        String downloaderScript,
                        Set<Long> superUsers, Set<Long> allowedUsers) {
}
