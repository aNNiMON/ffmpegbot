package com.annimon.ffmpegbot.commands.ytdlp;

import com.annimon.ffmpegbot.session.YtDlpSession;

import java.io.IOException;

public class YtDlpTask {

    public void process(YtDlpSession session) {
        final var commandBuilder = new YtDlpCommandBuilder();
        try {
            final ProcessBuilder pb = new ProcessBuilder(commandBuilder.buildCommand(session));
            pb.redirectErrorStream(true);
            pb.inheritIO();
            final Process process = pb.start();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
