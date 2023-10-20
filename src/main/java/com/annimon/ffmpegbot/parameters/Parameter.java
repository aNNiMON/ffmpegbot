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

    public int getPossibleValuesSize() {
        return possibleValues.size();
    }


    public abstract <I> void accept(Visitor<I> visitor, I input);

    public final String describe() {
        return displayName + ": " + describeValue(value);
    }

    public final String describeValueByIndex(int index) {
        return describeValue(safeGet(index));
    }

    protected String describeValue(T valueToDescribe) {
        return Objects.toString(valueToDescribe);
    }

    public void select(int index) {
        value = safeGet(index);
    }

    public int defaultColumnsCount() {
        return 2;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + displayName + ": " + value;
    }

    private T safeGet(int index) {
        final int size = possibleValues.size();
        if (index >= size) index = size - 1;
        else if (index < 0) index = 0;
        return possibleValues.get(index);
    }
}
