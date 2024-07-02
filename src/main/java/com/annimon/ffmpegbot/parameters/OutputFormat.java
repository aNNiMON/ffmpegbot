package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;

public class OutputFormat extends StringParameter {
    public static final String ID = "output";

    public static final String VIDEO = "VIDEO";
    public static final String AUDIO = "AUDIO";
    public static final String VIDEO_NOTE = "VIDEO NOTE";

    public OutputFormat(List<String> values, String initialValue) {
        super(ID, "➡\uFE0F Output", values, initialValue);
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
