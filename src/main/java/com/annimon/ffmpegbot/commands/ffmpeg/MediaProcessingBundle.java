package com.annimon.ffmpegbot.commands.ffmpeg;

import com.annimon.ffmpegbot.file.FileDownloadException;
import com.annimon.ffmpegbot.file.FileDownloader;
import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.file.FilePath;
import com.annimon.ffmpegbot.session.MediaSession;
import com.annimon.ffmpegbot.session.Resolver;
import com.annimon.ffmpegbot.session.Sessions;
import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.commands.CommandBundle;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.SimpleCallbackQueryCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.CallbackQueryContext;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.annimon.ffmpegbot.Permissions.ALLOWED_USERS;
import static com.annimon.ffmpegbot.commands.ffmpeg.CallbackQueryCommands.*;
import static com.annimon.ffmpegbot.commands.ffmpeg.MediaProcessingKeyboard.createKeyboard;

public class MediaProcessingBundle implements CommandBundle<For> {

    private final Sessions sessions;
    private final FileDownloader fileDownloader;

    public MediaProcessingBundle(Sessions sessions, FileDownloader fileDownloader) {
        this.sessions = sessions;
        this.fileDownloader = fileDownloader;
    }

    @Override
    public void register(@NotNull CommandRegistry commands) {
        commands.register(new SimpleCallbackQueryCommand(PREV, ALLOWED_USERS, ctx -> toggleParameter(ctx, true)));
        commands.register(new SimpleCallbackQueryCommand(NEXT, ALLOWED_USERS, ctx -> toggleParameter(ctx, false)));
        commands.register(new SimpleCallbackQueryCommand(DETAIL, ALLOWED_USERS, sessionCommand(this::details)));
        commands.register(new SimpleCallbackQueryCommand(PROCESS, ALLOWED_USERS, sessionCommand(this::process)));
    }

    public void handleMessage(final @NotNull CommonAbsSender sender, final @NotNull Message message) {
        final var fileInfo = Resolver.resolveFileInfo(message);
        if (fileInfo == null) return;

        final var session = new MediaSession();
        session.setChatId(message.getChatId());
        session.fromFileInfo(fileInfo);
        session.setParams(Resolver.resolveParameters(fileInfo.fileType()));

        final var result = Methods.sendMessage()
                .setChatId(message.getChatId())
                .setText(session.toString())
                .enableHtml()
                .setReplyToMessageId(message.getMessageId())
                .call(sender);

        session.setMessageId(result.getMessageId());
        sessions.put(session);

        Methods.editMessageReplyMarkup(result.getChatId(), result.getMessageId())
                .setReplyMarkup(createKeyboard(session))
                .call(sender);
    }

    private void toggleParameter(final CallbackQueryContext ctx, final boolean toLeft) {
        final var msg = ctx.message();
        if (msg == null) return;

        final var session = sessions.get(msg.getChatId(), msg.getMessageId());
        if (session == null) return;

        final String id = ctx.argument(0);
        final var parameters = session.getParams().stream()
                .filter(p -> p.getId().equals(id))
                .collect(Collectors.toSet());
        if (parameters.isEmpty()) return;

        for (Parameter<?> param : parameters) {
            if (toLeft) {
                param.toggleLeft();
            } else {
                param.toggleRight();
            }
        }
        editMessage(ctx, session);
    }

    private void details(final CallbackQueryContext ctx, final MediaSession session) {
        final String id = ctx.argument(0);
        session.getParams().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .ifPresent(p ->
                        ctx.answerAsAlert(p.describe()).callAsync(ctx.sender));
    }

    private void process(final CallbackQueryContext ctx, final MediaSession session) {
        if (!session.isDownloaded()) {
            sendAction(ctx, session);
            download(ctx, session);
        }
        if (!session.isDownloaded()) {
            session.setStatus("The file is not downloaded yet");
            editMessage(ctx, session);
            return;
        }

        sendAction(ctx, session);
        CompletableFuture.runAsync(() -> new FFmpegTask().process(session))
                .thenRunAsync(() -> {
                    editMessage(ctx, session);
                    Resolver.resolveMethod(session.getFileType())
                            .setChatId(session.getChatId())
                            .setFile(session.getOutputFile())
                            .call(ctx.sender);
                })
                .exceptionallyAsync(throwable -> {
                    editMessage(ctx, session);
                    return null;
                });
    }

    private void download(final CallbackQueryContext ctx, final MediaSession session) {
        try {
            final String defaultFilename = FilePath.defaultFilename(session.getFileType());
            final var file = fileDownloader.downloadFile(ctx.sender, session.getFileId(), defaultFilename);
            final var filename = FilePath.generateFilename(session.getFileId(), file.getName());
            session.setInputFile(FilePath.inputFile(filename));
            file.renameTo(session.getInputFile());
            session.setOutputFile(FilePath.outputFile(filename));
        } catch (FileDownloadException e) {
            session.setStatus("Unable to download due to " + e.getMessage());
            editMessage(ctx, session);
        }
    }

    private static void editMessage(CallbackQueryContext ctx, MediaSession session) {
        ctx.editMessage(session.toString())
                .enableHtml()
                .setReplyMarkup(createKeyboard(session))
                .callAsync(ctx.sender);
    }

    private static void sendAction(CallbackQueryContext ctx, MediaSession session) {
        Methods.sendChatAction(session.getChatId(), Resolver.resolveAction(session.getFileType()))
                .callAsync(ctx.sender);
    }

    private Consumer<CallbackQueryContext> sessionCommand(BiConsumer<CallbackQueryContext, MediaSession> consumer) {
        return ctx -> {
            final var msg = ctx.message();
            if (msg == null) return;

            final var session = sessions.get(msg.getChatId(), msg.getMessageId());
            if (session == null) return;

            consumer.accept(ctx, session);
        };
    }
}
