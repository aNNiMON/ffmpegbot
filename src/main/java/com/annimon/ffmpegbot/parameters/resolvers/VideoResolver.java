package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.*;
import com.annimon.ffmpegbot.session.FileInfo;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class VideoResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull List<Parameter<?>> parameters, @NotNull FileInfo fileInfo) {
        final boolean hasVideo = switch (fileInfo.fileType()) {
            case ANIMATION, VIDEO, VIDEO_NOTE -> true;
            default -> false;
        };

        if (hasVideo) {
            parameters.addAll(List.of(
                    new VideoBitrate(),
                    new VideoScale(),
                    new VideoFrameRate()
            ));
        }
    }
}
