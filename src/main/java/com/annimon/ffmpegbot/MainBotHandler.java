package com.annimon.ffmpegbot;

import com.annimon.ffmpegbot.commands.HelpCommand;
import com.annimon.ffmpegbot.commands.admin.AdminCommandBundle;
import com.annimon.ffmpegbot.commands.ffmpeg.InputParametersBundle;
import com.annimon.ffmpegbot.commands.ffmpeg.MediaProcessingBundle;
import com.annimon.ffmpegbot.commands.ytdlp.YtDlpCommandBundle;
import com.annimon.ffmpegbot.file.FallbackFileDownloader;
import com.annimon.ffmpegbot.file.TelegramClientFileDownloader;
import com.annimon.ffmpegbot.file.TelegramFileDownloader;
import com.annimon.ffmpegbot.session.Sessions;
import com.annimon.tgbotsmodule.BotHandler;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.authority.For;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.EnumSet;

public class MainBotHandler extends BotHandler {
    private final BotConfig botConfig;
    private final Permissions permissions;
    private final CommandRegistry<For> commands;
    private final MediaProcessingBundle mediaProcessingBundle;

    public MainBotHandler(BotConfig botConfig) {
        this.botConfig = botConfig;
        permissions = new Permissions(botConfig.superUsers(), botConfig.allowedUsers());
        commands = new CommandRegistry<>(this, this::checkAccess);
        final var sessions = new Sessions();
        final var fallbackFileDownloader = new FallbackFileDownloader(
                new TelegramFileDownloader(),
                new TelegramClientFileDownloader(
                        botConfig.downloaderScript(),
                        botConfig.appId(), botConfig.appHash(),
                        botConfig.botToken(), botConfig.botUsername())
        );
        mediaProcessingBundle = new MediaProcessingBundle(sessions, fallbackFileDownloader);
        commands.registerBundle(mediaProcessingBundle);
        commands.registerBundle(new InputParametersBundle(sessions));
        commands.registerBundle(new YtDlpCommandBundle(sessions));
        commands.registerBundle(new AdminCommandBundle());
        commands.register(new HelpCommand());
    }

    @Override
    protected BotApiMethod<?> onUpdate(@NotNull Update update) {
        if (commands.handleUpdate(update)) {
            return null;
        }
        if (update.hasMessage() && permissions.isUserAllowed(update.getMessage().getFrom().getId())) {
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
        return permissions.hasAccess(userId, roles);
    }

    @Override
    public void handleTelegramApiException(TelegramApiException ex) {
        throw new TelegramRuntimeException(ex);
    }
}
