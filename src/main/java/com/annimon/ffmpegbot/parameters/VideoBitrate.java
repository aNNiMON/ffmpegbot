package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;

public class VideoBitrate extends StringParameter {
    private static final List<String> VALUES = List.of(
            "16k", "32k", "", "64k", "128k", "256k",
            "512k", "1M", "2M", "4M", "8M", "16M"
    );

    public VideoBitrate() {
        super("vbitrate", "Video bitrate", VALUES, "");
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
