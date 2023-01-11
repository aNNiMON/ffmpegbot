package com.annimon.ffmpegbot.commands.ytdlp;

import com.annimon.ffmpegbot.Permissions;
import com.annimon.ffmpegbot.session.*;
import com.annimon.tgbotsmodule.commands.CommandBundle;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.SimpleRegexCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.RegexMessageContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class YtDlpCommandBundle implements CommandBundle<For> {
    @Override
    public void register(@NotNull CommandRegistry commands) {
        commands.register(new SimpleRegexCommand(
                Pattern.compile("/dl (https?://[^ ]+) ?(audio|\\d+)?p?"),
                Permissions.ALLOWED_USERS,
                this::download));
    }

    private void download(@NotNull RegexMessageContext ctx) {
        final String url = ctx.group(1);
        final String downloadOption = Optional.ofNullable(ctx.group(2))
                .filter(Predicate.not(String::isBlank))
                .orElse("720");
        final var fileType = downloadOption.equals("audio") ? FileType.AUDIO : FileType.VIDEO;
        final var ytDlpSession = new YtDlpSession(url, downloadOption, fileType);
        final var filename = FilePath.generateFilename(url, Resolver.resolveDefaultFilename(fileType));
        ytDlpSession.setOutputFilename(filename);

        CompletableFuture.runAsync(() -> new YtDlpTask().process(ytDlpSession))
                .thenRunAsync(() -> {
                    final File outputFile = FilePath.outputFile(ytDlpSession.getOutputFilename());
                    if (!outputFile.exists()) {
                        throw new RuntimeException("No file to send. Check your command settings.");
                    }
                    Resolver.resolveMethod(fileType)
                            .setChatId(ctx.chatId())
                            .setFile(outputFile)
                            .call(ctx.sender);
                })
                .exceptionallyAsync(throwable -> {
                    ctx.replyToMessage("Failed due to " + throwable.getMessage())
                            .callAsync(ctx.sender);
                    return null;
                });
    }
}
