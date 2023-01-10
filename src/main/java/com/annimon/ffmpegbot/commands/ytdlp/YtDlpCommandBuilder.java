package com.annimon.ffmpegbot.commands.ytdlp;

import com.annimon.ffmpegbot.session.FilePath;
import com.annimon.ffmpegbot.session.YtDlpSession;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class YtDlpCommandBuilder {

    public String[] buildCommand(final @NotNull YtDlpSession session) {
        final var commands = new ArrayList<String>();
        commands.add("yt-dlp");
        // Format
        commands.add("-f");
        String downloadOption = session.getDownloadOption();
        if (downloadOption.equals("audio")) {
            commands.add("bestaudio[ext=m4a]/bestaudio");
        } else {
            final var mp4 = "bestvideo[ext=mp4][height<=%s]+bestaudio[ext=m4a]".formatted(downloadOption);
            final var any = "bestvideo[height<=%s]+bestaudio".formatted(downloadOption);
            final var other = "best";
            commands.add(String.join("/", List.of(mp4, any, other)));
        }
        // Url
        commands.add(session.getUrl());
        // Output
        commands.add("-o");
        commands.add(FilePath.outputDir() + "/" + session.getOutputFilename());
        System.out.println(String.join(" ", commands));
        return commands.toArray(String[]::new);
    }
}
