package com.annimon.ffmpegbot.commands.ffmpeg;

import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.session.MediaSession;

import java.io.IOException;
import java.util.Scanner;

public class FFmpegTask {

    public void process(MediaSession session) {
        final var ffmpeg = new FFmpegCommandBuilder();
        for (Parameter<?> p : session.getParams()) {
            p.accept(ffmpeg, session);
        }

        try {
            final ProcessBuilder pb = new ProcessBuilder(ffmpeg.buildCommand(session));
            pb.redirectErrorStream(true);
            pb.inheritIO();
            session.setStatus("Starting ffmpeg");
            final Process process = pb.start();
            final Scanner out = new Scanner(process.getInputStream());
            while (out.hasNextLine()) {
                final String line = out.nextLine();
                session.setStatus(line);
            }
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            session.setStatus("Failed due to " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
