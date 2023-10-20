package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;

public class AudioEffect extends StringParameter {
    public static final String ROBOT = "Robot";
    public static final String ECHO = "Echo";
    public static final String ECHO_2 = "Echo 2";
    public static final String PULSATOR = "Pulsator";
    public static final String VIBRATO = "Vibrato";

    private static final List<String> VALUES = List.of(
            "", ROBOT, ECHO, ECHO_2, PULSATOR, VIBRATO
    );

    public AudioEffect() {
        super("aeffect", "Audio effect", VALUES, "");
    }

    @Override
    public String describeValue(String value) {
        if (value.isEmpty()) {
            return "NONE";
        } else {
            return super.describeValue(value);
        }
    }

    @Override
    public int defaultColumnsCount() {
        return 3;
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
