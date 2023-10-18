package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.OutputFormat;
import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static com.annimon.ffmpegbot.parameters.OutputFormat.*;

public class OutputFormatResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull List<Parameter<?>> parameters, @NotNull FileInfo fileInfo) {
        final var outputFormat = switch (fileInfo.fileType()) {
            case VIDEO -> new OutputFormat(List.of(VIDEO, AUDIO), VIDEO);
            case VIDEO_NOTE -> new OutputFormat(List.of(VIDEO_NOTE, VIDEO, AUDIO), VIDEO_NOTE);
            default -> null;
        };
        if (outputFormat != null) {
            parameters.add(outputFormat);
        }
    }
}
