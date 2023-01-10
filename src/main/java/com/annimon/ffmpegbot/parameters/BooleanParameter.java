package com.annimon.ffmpegbot.parameters;

import java.util.List;

public abstract class BooleanParameter extends Parameter<Boolean> {
    public BooleanParameter(String id, String name, Boolean value) {
        super(id, name, List.of(false, true), value);
    }

    @Override
    public String describe() {
        if (value) {
            return describeValue("ON");
        } else {
            return describeValue("OFF");
        }
    }

    @Override
    protected void toggle(int dir) {
        value = !value;
    }
}
