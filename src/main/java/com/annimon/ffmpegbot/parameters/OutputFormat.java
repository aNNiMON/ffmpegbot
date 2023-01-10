package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;

public class OutputFormat extends StringParameter {
    public static final String VIDEO = "VIDEO";
    public static final String AUDIO = "AUDIO";
    public static final String VIDEO_NOTE = "VIDEO NOTE";

    public static OutputFormat forVideo() {
        return new OutputFormat(List.of(VIDEO, AUDIO), VIDEO);
    }

    public static OutputFormat forVideoNote() {
        return new OutputFormat(List.of(VIDEO_NOTE, VIDEO, AUDIO), VIDEO_NOTE);
    }

    public OutputFormat(List<String> values, String initialValue) {
        super("output", "Output", values, initialValue);
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
