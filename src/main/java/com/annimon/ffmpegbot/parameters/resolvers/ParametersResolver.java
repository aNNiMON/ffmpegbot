package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.Parameters;
import com.annimon.ffmpegbot.session.FileInfo;
import org.jetbrains.annotations.NotNull;

public interface ParametersResolver {

    void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo);
}
