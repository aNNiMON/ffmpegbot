package com.annimon.ffmpegbot.parameters;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public class Parameters extends AbstractCollection<Parameter<?>> {

    private final Map<String, Parameter<?>> parameters;

    public Parameters() {
        parameters = new LinkedHashMap<>();
    }

    @Override
    public boolean add(Parameter<?> parameter) {
        return !Objects.equals(parameter, parameters.put(parameter.id, parameter));
    }

    public Parameter<?> findById(String id) {
        return parameters.get(id);
    }

    @NotNull
    @Override
    public Iterator<Parameter<?>> iterator() {
        return parameters.values().iterator();
    }

    @Override
    public int size() {
        return parameters.size();
    }
}
