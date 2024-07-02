package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.Parameters;
import com.annimon.ffmpegbot.parameters.SpeedFactor;
import com.annimon.ffmpegbot.session.FileInfo;
import org.jetbrains.annotations.NotNull;

public class SpeedFactorResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo) {
        // For anything except static images
        parameters.add(new SpeedFactor());
    }
}
