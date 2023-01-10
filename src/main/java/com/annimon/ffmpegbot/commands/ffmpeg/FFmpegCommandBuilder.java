package com.annimon.ffmpegbot.commands.ffmpeg;

import com.annimon.ffmpegbot.parameters.*;
import com.annimon.ffmpegbot.session.FilePath;
import com.annimon.ffmpegbot.session.FileType;
import com.annimon.ffmpegbot.session.FileTypes;
import com.annimon.ffmpegbot.session.MediaSession;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FFmpegCommandBuilder implements Visitor<MediaSession> {

    private boolean discardAudio;
    private final List<String> audioCommands;
    private final List<String> videoCommands;
    private final List<String> audioFilters;
    private final List<String> videoFilters;

    public FFmpegCommandBuilder() {
        audioCommands = new ArrayList<>();
        videoCommands = new ArrayList<>();
        audioFilters = new ArrayList<>();
        videoFilters = new ArrayList<>();
    }

    @Override
    public void visit(DisableAudio p, MediaSession session) {
        discardAudio = p.getValue();
        if (discardAudio) {
            audioCommands.add("-an");
        }
    }

    @Override
    public void visit(AudioBitrate p, MediaSession session) {
        if (discardAudio) return;
        if (p.getValue().isEmpty()) return;
        audioCommands.add("-b:a");
        audioCommands.add(p.getValue());
    }

    @Override
    public void visit(AudioEffect p, MediaSession input) {
        if (discardAudio) return;
        if (p.getValue().isEmpty()) return;
        audioFilters.add(switch (p.getValue()) {
            case AudioEffect.ECHO -> "aecho=0.8:0.9:40|50|70:0.4|0.3|0.2";
            case AudioEffect.ECHO_2 -> "aecho=0.8:0.9:500|1000:0.2|0.1";
            case AudioEffect.PULSATOR -> "apulsator=mode=sine:hz=0.5";
            case AudioEffect.VIBRATO -> "vibrato=f=4";
            default /* AudioEffect.ROBOT */ -> "afftfilt=\"" +
                    "real='hypot(re,im)*sin(0)'" +
                    ":imag='hypot(re,im)*cos(0)'" +
                    ":win_size=512:overlap=0.75\"";
        });
    }

    @Override
    public void visit(AudioPitch p, MediaSession input) {
        if (discardAudio) return;
        if (p.getValue().equals("1")) return;
        audioFilters.add("rubberband=pitchq=quality:pitch=" + p.getValue());
    }

    @Override
    public void visit(AudioVolume p, MediaSession session) {
        if (discardAudio) return;
        if (p.getValue().isEmpty()) return;
        audioFilters.add("volume=" + p.getValue());
    }

    @Override
    public void visit(SpeedFactor p, MediaSession input) {
        if (p.getValue().equals("1")) return;
        if (!discardAudio) {
            audioFilters.add("atempo=" + p.getValue());
        }
        videoFilters.add("setpts=PTS/" + p.getValue());
    }

    @Override
    public void visit(VideoBitrate p, MediaSession session) {
        if (p.getValue().isEmpty()) return;
        videoCommands.add("-b:v");
        videoCommands.add(p.getValue());
    }

    @Override
    public void visit(VideoScale p, MediaSession session) {
        if (p.getValue().isEmpty()) return;
        videoFilters.add("scale=-2:" + p.getValue());
    }

    @Override
    public void visit(VideoFrameRate p, MediaSession input) {
        if (p.getValue().isEmpty()) return;
        videoFilters.add("fps=" + p.getValue());
    }

    @Override
    public void visit(OutputFormat p, MediaSession session) {
        final String localFilename = session.getInputFile().getName();
        String additionalExtension = "";

        switch (p.getValue()) {
            case OutputFormat.VIDEO -> {
                session.setFileType(FileType.VIDEO);
                additionalExtension = ".mp4";
            }
            case OutputFormat.VIDEO_NOTE -> {
                session.setFileType(FileType.VIDEO_NOTE);
                additionalExtension = ".mp4";
            }
            case OutputFormat.AUDIO -> {
                session.setFileType(FileType.AUDIO);
                additionalExtension = ".mp3";
            }
        }

        if (localFilename.toLowerCase(Locale.ENGLISH).endsWith(additionalExtension)) {
            additionalExtension = "";
        }
        session.setOutputFile(FilePath.outputFile(localFilename + additionalExtension));
    }

    public String[] buildCommand(final @NotNull MediaSession session) {
        final var commands = new ArrayList<String>();
        commands.addAll(List.of("ffmpeg", "-loglevel", "quiet", "-stats"));
        commands.addAll(buildTrim(session));
        commands.addAll(List.of("-i", FilePath.inputDir() + "/" + session.getInputFile().getName()));
        if (FileTypes.canContainAudio(session.getFileType())) {
            commands.addAll(audioCommands);
            if (!audioFilters.isEmpty()) {
                commands.add("-af");
                commands.add(String.join(",", audioFilters));
            }
        }
        if (FileTypes.canContainVideo(session.getFileType())) {
            commands.addAll(videoCommands);
            if (!videoFilters.isEmpty()) {
                commands.add("-vf");
                commands.add(String.join(",", videoFilters));
            }
        }
        commands.addAll(List.of("-y", FilePath.outputDir() + "/" + session.getOutputFile().getName()));
        return commands.toArray(String[]::new);
    }

    private List<String> buildTrim(@NotNull MediaSession session) {
        final var commands = new ArrayList<String>();
        final var inputParams = session.getInputParams();
        if (!inputParams.getStartPosition().isEmpty()) {
            commands.add("-ss");
            commands.add(inputParams.getStartPosition());
        }
        if (!inputParams.getEndPosition().isEmpty()) {
            commands.add("-to");
            commands.add(inputParams.getEndPosition());
        }
        if (!inputParams.getDuration().isEmpty()) {
            commands.add("-t");
            commands.add(inputParams.getDuration());
        }
        return commands;
    }
}