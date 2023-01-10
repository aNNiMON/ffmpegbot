package com.annimon.ffmpegbot.commands.ffmpeg;

import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.session.FilePath;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.annimon.ffmpegbot.commands.ffmpeg.CallbackQueryCommands.*;
import static com.annimon.ffmpegbot.commands.ffmpeg.MediaProcessingKeyboard.createKeyboard;

public class MediaProcessingBundle implements CommandBundle<For> {

    private final Sessions sessions;

    public MediaProcessingBundle(Sessions sessions) {
        this.sessions = sessions;
    }

    @Override
    public void register(@NotNull CommandRegistry commands) {
        commands.register(new SimpleCallbackQueryCommand(PREV, For.ADMIN, ctx -> toggleParameter(ctx, true)));
        commands.register(new SimpleCallbackQueryCommand(NEXT, For.ADMIN, ctx -> toggleParameter(ctx, false)));
        commands.register(new SimpleCallbackQueryCommand(DETAIL, For.ADMIN, sessionCommand(this::details)));
        commands.register(new SimpleCallbackQueryCommand(PROCESS, For.ADMIN, sessionCommand(this::process)));
    }

    public void handleMessage(final @NotNull CommonAbsSender sender, final @NotNull Message message) {
        final var fileInfo = Resolver.resolveFileInfo(message);
        if (fileInfo == null) return;

        final var session = new MediaSession();
        session.setChatId(message.getChatId());
        session.setFileId(fileInfo.fileId());
        session.setFileType(fileInfo.fileType());
        session.setOriginalFilename(fileInfo.filename());
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
            download(ctx, session);
        }
        if (!session.isDownloaded()) {
            session.setStatus("The file is not downloaded yet");
            editMessage(ctx, session);
            return;
        }
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
            final var tgFile = Methods.getFile(session.getFileId()).call(ctx.sender);
            final var localFilename = FilePath.generateFilename(tgFile.getFileId(), tgFile.getFilePath());
            session.setInputFile(ctx.sender.downloadFile(tgFile, FilePath.inputFile(localFilename)));
            session.setOutputFile(FilePath.outputFile(localFilename));
        } catch (TelegramApiException e) {
            session.setStatus("Unable to download due to " + e.getMessage());
            editMessage(ctx, session);
        }
    }

    private void editMessage(CallbackQueryContext ctx, MediaSession session) {
        ctx.editMessage(session.toString())
                .enableHtml()
                .setReplyMarkup(createKeyboard(session))
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
