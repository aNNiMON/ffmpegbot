package com.annimon.ffmpegbot.commands.ytdlp;

import com.annimon.ffmpegbot.Permissions;
import com.annimon.ffmpegbot.commands.ffmpeg.CallbackQueryCommands;
import com.annimon.ffmpegbot.file.FilePath;
import com.annimon.ffmpegbot.session.*;
import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.commands.CommandBundle;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.SimpleCallbackQueryCommand;
import com.annimon.tgbotsmodule.commands.SimpleRegexCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.CallbackQueryContext;
import com.annimon.tgbotsmodule.commands.context.RegexMessageContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.annimon.ffmpegbot.commands.ffmpeg.MediaProcessingKeyboard.createKeyboard;

public class YtDlpCommandBundle implements CommandBundle<For> {

    private final Sessions sessions;

    public YtDlpCommandBundle(Sessions sessions) {
        this.sessions = sessions;
    }

    @Override
    public void register(@NotNull CommandRegistry commands) {
        commands.register(new SimpleRegexCommand(
                Pattern.compile("/dl (https?://[^ ]+) ?(audio|best|\\d+)?"),
                Permissions.ALLOWED_USERS,
                this::download));
        commands.register(new SimpleCallbackQueryCommand(
                CallbackQueryCommands.YTDLP_INFO,
                Permissions.ALLOWED_USERS,
                sessionCommand(this::ytDlpInfo)));
        commands.register(new SimpleCallbackQueryCommand(
                CallbackQueryCommands.YTDLP_START,
                Permissions.ALLOWED_USERS,
                sessionCommand(this::ytDlpStart)));
    }

    private void download(@NotNull RegexMessageContext ctx) {
        final String url = ctx.group(1);
        final String downloadOption = Optional.ofNullable(ctx.group(2))
                .filter(Predicate.not(String::isBlank))
                .orElse("best");

        final var message = ctx.message();
        final var fileType = downloadOption.equals("audio") ? FileType.AUDIO : FileType.VIDEO;
        final var filename = FilePath.generateFilename(url, FilePath.defaultFilename(fileType));

        final var session = new YtDlpSession(url, downloadOption, fileType);
        session.setChatId(message.getChatId());
        session.setOutputFilename(filename);

        final var result = ctx.replyToMessage(session.toString())
                .enableHtml()
                .call(ctx.sender);
        session.setMessageId(result.getMessageId());
        sessions.put(session);

        Methods.editMessageReplyMarkup(result.getChatId(), result.getMessageId())
                .setReplyMarkup(createKeyboard(session))
                .call(ctx.sender);
    }

    private void ytDlpInfo(CallbackQueryContext ctx, YtDlpSession session) {
        CompletableFuture.runAsync(() -> new YtDlpTask().getInfo(session))
                .thenRun(() -> {
                    Methods.editMessageText()
                            .setChatId(session.getChatId())
                            .setMessageId(session.getMessageId())
                            .setText(session.toString())
                            .enableHtml()
                            .disableWebPagePreview()
                            .setReplyMarkup(createKeyboard(session))
                            .callAsync(ctx.sender);
                })
                .exceptionally(throwable -> {
                    ctx.answerAsAlert("Failed due to " + throwable.getMessage())
                            .callAsync(ctx.sender);
                    return null;
                });
    }

    private void ytDlpStart(CallbackQueryContext ctx, YtDlpSession session) {
        Methods.sendChatAction(ctx.message().getChatId(), Resolver.resolveAction(session.getFileType()))
                .callAsync(ctx.sender);
        CompletableFuture.runAsync(() -> new YtDlpTask().process(session))
                .thenRunAsync(() -> {
                    final File outputFile = FilePath.outputFile(session.getOutputFilename());
                    if (!outputFile.exists()) {
                        throw new RuntimeException("No file to send. Check your command settings.");
                    }
                    Resolver.resolveMethod(session.getFileType())
                            .setChatId(session.getChatId())
                            .setFile(outputFile)
                            .call(ctx.sender);
                })
                .exceptionallyAsync(throwable -> {
                    ctx.answerAsAlert("Failed due to " + throwable.getMessage())
                            .callAsync(ctx.sender);
                    return null;
                });
    }

    private Consumer<CallbackQueryContext> sessionCommand(BiConsumer<CallbackQueryContext, YtDlpSession> consumer) {
        return ctx -> {
            final var msg = ctx.message();
            if (msg == null) return;

            final var session = sessions.getYtDlpSession(msg.getChatId(), msg.getMessageId());
            if (session == null) return;

            consumer.accept(ctx, session);
        };
    }
}
