package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OutputFormat extends StringParameter {
    public static final String ID = "output";

    public static final String VIDEO = "VIDEO";
    public static final String AUDIO = "AUDIO";
    public static final String VIDEO_NOTE = "VIDEO NOTE";

    public OutputFormat(List<String> values, String initialValue) {
        super(ID, "➡️ Output", values, initialValue);
    }

    public OutputFormat disableFormat(String format) {
        if (possibleValues.size() <= 1) return this;
        final var values = possibleValues.stream()
                .filter(f -> !Objects.equals(f, format))
                .map(Objects::toString)
                .toList();
        if (possibleValues.size() == values.size()) {
            return this;
        }
        return new OutputFormat(values, values.get(0));
    }

    public OutputFormat enableFormat(String format) {
        boolean contains = possibleValues.stream()
                .anyMatch(f -> Objects.equals(f, format));
        if (contains) return this;

        final var values = new ArrayList<String>(possibleValues);
        values.add(format);
        return new OutputFormat(values, values.get(0));
    }

    @Override
    public <I> void accept(Visitor<I> visitor, I input) {
        visitor.visit(this, input);
    }
}
