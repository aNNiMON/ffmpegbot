package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.parameters.SpeedFactor;
import com.annimon.ffmpegbot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class SpeedFactorResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull List<Parameter<?>> parameters, @NotNull FileInfo fileInfo) {
        // For anything except static images
        parameters.add(new SpeedFactor());
    }
}
