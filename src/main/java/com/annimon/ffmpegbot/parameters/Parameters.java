package com.annimon.ffmpegbot.parameters;

import java.util.List;

public class Parameters {

    public static List<Parameter<?>> forAudio() {
        return List.of(
                new AudioBitrate(),
                new AudioEffect(),
                new AudioPitch(),
                new AudioVolume(),
                new SpeedFactor()
        );
    }

    public static List<Parameter<?>> forAnimation() {
        return List.of(
                new VideoBitrate(),
                new VideoScale(),
                new VideoFrameRate(),
                new SpeedFactor()
        );
    }

    public static List<Parameter<?>> forVideo() {
        return List.of(
                new DisableAudio(),
                new AudioBitrate(),
                new AudioEffect(),
                new AudioPitch(),
                new AudioVolume(),
                new VideoBitrate(),
                new VideoScale(),
                new VideoFrameRate(),
                new SpeedFactor(),
                OutputFormat.forVideo()
        );
    }

    public static List<Parameter<?>> forVideoNote() {
        return List.of(
                new DisableAudio(),
                new AudioBitrate(),
                new AudioEffect(),
                new AudioPitch(),
                new AudioVolume(),
                new VideoBitrate(),
                new VideoScale(),
                new VideoFrameRate(),
                new SpeedFactor(),
                OutputFormat.forVideoNote()
        );
    }
}
