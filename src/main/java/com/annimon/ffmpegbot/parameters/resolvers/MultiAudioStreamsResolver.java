package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.AudioStreamByLanguage;
import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.session.FileInfo;
import com.annimon.ffmpegbot.session.FileType;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class MultiAudioStreamsResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull List<Parameter<?>> parameters, @NotNull FileInfo fileInfo) {
        // TODO: ffprobe check for actual codec
        if (!fileInfo.fileType().equals(FileType.VIDEO)) {
            return;
        }

        if (fileInfo.getExtension().equals("mkv")) {
            parameters.add(new AudioStreamByLanguage());
        }
    }
}
