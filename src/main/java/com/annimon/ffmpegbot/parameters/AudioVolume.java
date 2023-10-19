package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioVolume extends StringParameter {
    private static final List<String> VALUES = List.of(
            "-15dB", "-10dB", "-5dB", "-2dB", "", "2dB", "5dB", "10dB", "15dB"
    );

    public AudioVolume() {
        super("volume", "Volume", VALUES, "");
    }

    @Override
    public String describeValue(String value) {
        if (value.isEmpty()) {
            return "ORIGINAL";
        } else {
            return super.describeValue(value);
        }
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
