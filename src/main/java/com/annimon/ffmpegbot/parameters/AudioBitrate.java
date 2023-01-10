package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioBitrate extends StringParameter {
    private static final List<String> VALUES = List.of(
            "",
            "4k", "16k", "32k", "64k", "128k",
            "256k", "320k", "512k"
    );

    public AudioBitrate() {
        super("abitrate", "Audio bitrate", VALUES, "");
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
