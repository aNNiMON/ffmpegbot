package com.annimon.ffmpegbot.parameters;

import com.annimon.ffmpegbot.commands.ffmpeg.Visitor;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class Parameter<T> {
    protected final String id;
    protected final String displayName;
    protected final List<? extends T> possibleValues;
    protected T value;

    protected Parameter(String id, String displayName, List<? extends T> values, T value) {
        this.id = id;
        this.displayName = displayName;
        this.possibleValues = values;
        this.value = value;
        checkArgument(!values.isEmpty(), "possible values cannot be empty");
        checkArgument(values.contains(value), "possible values must contain a value");
    }

    public String getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public List<? extends T> getPossibleValues() {
        return possibleValues;
    }

    public abstract <I> void accept(Visitor<I> visitor, I input);

    public String describe() {
        return describeValue(Objects.toString(value));
    }

    protected final String describeValue(String customValue) {
        return displayName + ": " + customValue;
    }

    public void toggleLeft() {
        toggle(-1);
    }

    public void toggleRight() {
        toggle(+1);
    }

    protected void toggle(int dir) {
        final int size = possibleValues.size();
        if (size == 1) return;

        int nextIndex = possibleValues.indexOf(value) + dir;
        if (nextIndex >= size) nextIndex -= size;
        else if (nextIndex < 0) nextIndex += size;

        value = possibleValues.get(nextIndex);
    }

    @Override
    public String toString() {
        return "[" + id + "] " + displayName + ": " + value;
    }
}
