package com.annimon.ffmpegbot.parameters.resolvers;

import com.annimon.ffmpegbot.parameters.*;
import com.annimon.ffmpegbot.session.FileInfo;
import com.annimon.ffmpegbot.session.FileType;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AudioResolver implements ParametersResolver {

    @Override
    public void resolve(@NotNull Parameters parameters, @NotNull FileInfo fileInfo) {
        final boolean hasAudio = switch (fileInfo.fileType()) {
            case ANIMATION -> false;
            case AUDIO, VOICE -> true;
            case VIDEO, VIDEO_NOTE -> true; // TODO: add actual ffprobe check for audio
        };

        if (hasAudio) {
            disableAudioParam(parameters, fileInfo.fileType());
            parameters.addAll(List.of(
                    new AudioBitrate(),
                    new AudioCrystalizer(),
                    new AudioEffect(),
                    new AudioPitch(),
                    new AudioVolume()
            ));
        }
    }

    @Override
    public void refine(@NotNull Parameters parameters) {
        parameters.findById(DisableAudio.ID, DisableAudio.class)
                .ifPresent(p -> {
                    final Set<String> parameterIds = Set.of(
                            AudioBitrate.ID,
                            AudioCrystalizer.ID,
                            AudioEffect.ID,
                            AudioPitch.ID,
                            AudioVolume.ID
                    );
                    Optional<OutputFormat> outputFormat = parameters.findById(OutputFormat.ID, OutputFormat.class);
                    if (p.getValueAsPrimitive()) {
                        parameters.disableAll(parameterIds);
                        outputFormat.ifPresent(par -> parameters.add(par.disableFormat(OutputFormat.AUDIO)));
                    } else {
                        parameters.enableAll(parameterIds);
                        outputFormat.ifPresent(par -> parameters.add(par.enableFormat(OutputFormat.AUDIO)));
                    }
                });
    }

    private void disableAudioParam(@NotNull Parameters parameters, @NotNull FileType fileType) {
        final boolean canAudioBeDisabled = switch (fileType) {
            case ANIMATION, AUDIO, VOICE -> false;
            case VIDEO, VIDEO_NOTE -> true;
        };
        if (canAudioBeDisabled) {
            parameters.add(new DisableAudio());
        }
    }
}
