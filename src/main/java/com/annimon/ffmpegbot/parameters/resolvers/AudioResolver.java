package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.*;
import com.annimon.ffmpegbot.session.FileInfo;
import com.annimon.ffmpegbot.session.FileType;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AudioResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull List<Parameter<?>> parameters, @NotNull FileInfo fileInfo) {
        final boolean hasAudio = switch (fileInfo.fileType()) {
            case ANIMATION -> false;
            case AUDIO, VOICE -> true;
            case VIDEO, VIDEO_NOTE -> true; // TODO: add actual ffprobe check for audio
        };

        if (hasAudio) {
            disableAudioParam(parameters, fileInfo.fileType());
            parameters.addAll(List.of(
                    new AudioBitrate(),
                    new AudioEffect(),
                    new AudioPitch(),
                    new AudioVolume()
            ));
        }
    }

    private void disableAudioParam(@NotNull List<Parameter<?>> parameters, @NotNull FileType fileType) {
        final boolean canAudioBeDisabled = switch (fileType) {
            case ANIMATION, AUDIO, VOICE -> false;
            case VIDEO, VIDEO_NOTE -> true;
        };
        if (canAudioBeDisabled) {
            parameters.add(new DisableAudio());
        }
    }
}
