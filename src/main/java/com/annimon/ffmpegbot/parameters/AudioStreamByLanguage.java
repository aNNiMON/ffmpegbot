package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioStreamByLanguage extends StringParameter {
    private static final List<String> VALUES = List.of(
            "", "eng", "fra", "ita", "ukr", "spa"
    );

    public AudioStreamByLanguage() {
        super("asellang", "Audio Track", VALUES, "");
    }

    @Override
    public String describe() {
        if (value.isEmpty()) {
            return describeValue("AUTO");
        } else {
            return super.describe();
        }
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}