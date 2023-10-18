package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class MultiAudioStreamsResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull List<Parameter<?>> parameters, @NotNull FileInfo fileInfo) {
        // TODO: Disabled until files support will be implemented
        // Check for mkv file type
        // parameters.add(new AudioStreamByLanguage());
    }
}
