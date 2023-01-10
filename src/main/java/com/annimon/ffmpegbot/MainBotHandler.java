package com.annimon.ffmpegbot;

import com.annimon.ffmpegbot.commands.HelpCommand;
import com.annimon.ffmpegbot.commands.admin.AdminCommandBundle;
import com.annimon.ffmpegbot.commands.ffmpeg.InputParametersBundle;
import com.annimon.ffmpegbot.commands.ffmpeg.MediaProcessingBundle;
import com.annimon.ffmpegbot.commands.ytdlp.YtDlpCommandBundle;
import com.annimon.ffmpegbot.session.Sessions;
import com.annimon.tgbotsmodule.BotHandler;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.authority.For;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.EnumSet;

public class MainBotHandler extends BotHandler {
    private final BotConfig botConfig;
    private final CommandRegistry<For> commands;
    private final MediaProcessingBundle mediaProcessingBundle;

    public MainBotHandler(BotConfig botConfig) {
        this.botConfig = botConfig;
        commands = new CommandRegistry<>(this, this::checkAccess);
        final var sessions = new Sessions();
        mediaProcessingBundle = new MediaProcessingBundle(sessions);
        commands.registerBundle(mediaProcessingBundle);
        commands.registerBundle(new InputParametersBundle(sessions));
        commands.registerBundle(new YtDlpCommandBundle());
        commands.registerBundle(new AdminCommandBundle());
        commands.register(new HelpCommand());
    }

    @Override
    protected BotApiMethod<?> onUpdate(@NotNull Update update) {
        if (commands.handleUpdate(update)) {
            return null;
        }
        if (update.hasMessage()) {
            mediaProcessingBundle.handleMessage(this, update.getMessage());
        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return botConfig.botUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.botToken();
    }

    private boolean checkAccess(Update update, @NotNull User user, @NotNull EnumSet<For> roles) {
        final long userId = user.getId();

        if (roles.contains(For.CREATOR) && botConfig.superUsers().contains(userId))
            return true;
        if (roles.contains(For.ADMIN) && botConfig.isUserAllowed(userId))
            return true;
        return roles.contains(For.USER);
    }
}
