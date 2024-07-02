package com.annimon.ffmpegbot.parameters;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public class Parameters extends AbstractCollection<Parameter<?>> {

    private final Map<String, Parameter<?>> enabledParameters;
    private final Map<String, Parameter<?>> disabledParameters;

    public Parameters() {
        enabledParameters = new LinkedHashMap<>();
        disabledParameters = new LinkedHashMap<>();
    }

    @Override
    public boolean add(Parameter<?> parameter) {
        return !Objects.equals(parameter, enabledParameters.put(parameter.id, parameter));
    }

    public Parameter<?> findById(String id) {
        return enabledParameters.get(id);
    }

    public void disable(String id) {
        Optional.ofNullable(findById(id))
                .ifPresent(p -> disabledParameters.put(p.id, p));
    }

    public void enable(String id) {
        Optional.ofNullable(disabledParameters.get(id))
                .ifPresent(this::add);
    }

    @NotNull
    @Override
    public Iterator<Parameter<?>> iterator() {
        return enabledParameters.values().iterator();
    }

    @Override
    public int size() {
        return enabledParameters.size();
    }
}
