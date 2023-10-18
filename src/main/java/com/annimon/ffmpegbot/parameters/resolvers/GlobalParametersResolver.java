package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class GlobalParametersResolver implements ParametersResolver {
    private final List<ParametersResolver> resolvers;

    public GlobalParametersResolver() {
        resolvers = new ArrayList<>();
        resolvers.add(new AudioResolver());
        resolvers.add(new MultiAudioStreamsResolver());
        resolvers.add(new VideoResolver());
        resolvers.add(new SpeedFactorResolver());
        resolvers.add(new OutputFormatResolver());
    }

    @Override
    public void resolve(@NotNull List<Parameter<?>> parameters, @NotNull FileInfo fileInfo) {
        for (ParametersResolver resolver : resolvers) {
            resolver.resolve(parameters, fileInfo);
        }
    }
}
