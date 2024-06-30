package com.annimon.ffmpegbot.parameters;

import java.util.List;

public abstract class BooleanParameter extends Parameter<Boolean> {
    protected BooleanParameter(String id, String name, Boolean value) {
        super(id, name, List.of(false, true), value);
    }

    @Override
    public String describeValue(Boolean value) {
        if (value != null && value) {
            return "ON";
        } else {
            return "OFF";
        }
    }
}
