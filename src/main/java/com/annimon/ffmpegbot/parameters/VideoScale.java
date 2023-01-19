package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;

public class VideoScale extends StringParameter {
    private static final List<String> VALUES = List.of(
            "144", "240", "360", "", "480", "720", "1080"
    );

    public VideoScale() {
        super("scale", "Scale", VALUES, "");
    }

    @Override
    public String describe() {
        if (value.isEmpty()) {
            return describeValue("ORIGINAL");
        } else {
            return describeValue(value + "p");
        }
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
