package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface ParametersResolver {

    void resolve(@NotNull List<Parameter<?>> parameters, @NotNull FileInfo fileInfo);
}
