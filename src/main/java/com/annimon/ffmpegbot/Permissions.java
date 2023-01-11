package com.annimon.ffmpegbot;

import com.annimon.tgbotsmodule.commands.authority.For;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public record Permissions(Set<Long> superUsers, Set<Long> allowedUsers) {
    public static final EnumSet<For> SUPERUSERS = EnumSet.of(For.CREATOR);
    public static final EnumSet<For> ALLOWED_USERS = EnumSet.of(For.CREATOR, For.ADMIN);

    boolean hasAccess(long userId, @NotNull EnumSet<For> roles) {
        if (roles.contains(For.CREATOR) && superUsers().contains(userId))
            return true;
        if (roles.contains(For.ADMIN) && isUserAllowed(userId))
            return true;
        return roles.contains(For.USER);
    }

    boolean isUserAllowed(Long userId) {
        return superUsers().contains(userId)
                || allowedUsers().contains(userId);
    }
}
