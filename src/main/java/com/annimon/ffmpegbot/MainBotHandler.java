package com.annimon.ffmpegbot;

import com.annimon.ffmpegbot.commands.HelpCommand;
import com.annimon.ffmpegbot.commands.PrivacyCommand;
import com.annimon.ffmpegbot.commands.admin.AdminCommandBundle;
import com.annimon.ffmpegbot.commands.ffmpeg.InputParametersBundle;
import com.annimon.ffmpegbot.commands.ffmpeg.MediaProcessingBundle;
import com.annimon.ffmpegbot.commands.ytdlp.YtDlpCommandBundle;
import com.annimon.ffmpegbot.file.FallbackFileDownloader;
import com.annimon.ffmpegbot.file.TelegramClientFileDownloader;
import com.annimon.ffmpegbot.file.TelegramFileDownloader;
import com.annimon.ffmpegbot.session.Sessions;
import com.annimon.tgbotsmodule.BotHandler;
import com.annimon.tgbotsmodule.BotModuleOptions;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.authority.For;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MainBotHandler extends BotHandler {
    private final Permissions permissions;
    private final CommandRegistry<For> commands;
    private final MediaProcessingBundle mediaProcessingBundle;

    public MainBotHandler(BotConfig botConfig) {
        super(BotModuleOptions.createDefault(botConfig.botToken()));
        permissions = new Permissions(botConfig.superUserIds(), botConfig.allowedUserIds());
        commands = new CommandRegistry<>(botConfig.botUsername(), permissions);
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
        commands.registerBundle(new AdminCommandBundle(sessions));
        commands.register(new HelpCommand(permissions));
        commands.register(new PrivacyCommand());
    }

    @Override
    protected BotApiMethod<?> onUpdate(@NotNull Update update) {
        if (commands.handleUpdate(this, update)) {
            return null;
        }
        if (update.hasMessage() && permissions.isUserAllowed(update.getMessage().getFrom().getId())) {
            mediaProcessingBundle.handleMessage(this, update.getMessage());
        }
        return null;
    }

    @Override
    public void handleTelegramApiException(TelegramApiException ex) {
        throw new TelegramRuntimeException(ex);
    }
}
