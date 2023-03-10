package com.annimon.ffmpegbot.session;

import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.parameters.Parameters;
import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.api.methods.interfaces.MediaMessageMethod;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class Resolver {
    public static FileInfo resolveFileInfo(@NotNull Message message) {
        if (message.hasAnimation()) {
            final var att = message.getAnimation();
            return new FileInfo(FileType.ANIMATION, att.getFileId(), att.getFileName(),
                    att.getFileSize(), att.getDuration(), att.getWidth(), att.getHeight());
        } else if (message.hasAudio()) {
            final var att = message.getAudio();
            return new FileInfo(FileType.AUDIO, att.getFileId(), att.getFileName(), att.getFileSize(), att.getDuration());
        } else if (message.hasVideo()) {
            final var att = message.getVideo();
            return new FileInfo(FileType.VIDEO, att.getFileId(), att.getFileName(),
                    att.getFileSize(), att.getDuration(), att.getWidth(), att.getHeight());
        } else if (message.hasVideoNote()) {
            final var att = message.getVideoNote();
            final Long fileSize = att.getFileSize() != null ? (Long.valueOf(att.getFileSize())) : null;
            return new FileInfo(FileType.VIDEO_NOTE, att.getFileId(), null,
                    fileSize, att.getDuration(), att.getLength(), att.getLength());
        } else if (message.hasVoice()) {
            final var att = message.getVoice();
            return new FileInfo(FileType.VOICE, att.getFileId(), null, att.getFileSize(), att.getDuration());
        } else {
            return null;
        }
    }

    public static List<Parameter<?>> resolveParameters(@NotNull FileType fileType) {
        return switch (fileType) {
            case ANIMATION -> Parameters.forAnimation();
            case VIDEO -> Parameters.forVideo();
            case VIDEO_NOTE -> Parameters.forVideoNote();
            case AUDIO, VOICE -> Parameters.forAudio();
        };
    }

    public static MediaMessageMethod<? extends MediaMessageMethod<?, ?>, ?> resolveMethod(@NotNull FileType fileType) {
        return switch (fileType) {
            case ANIMATION -> Methods.sendAnimation();
            case AUDIO -> Methods.sendAudio();
            case VIDEO -> Methods.sendVideo();
            case VIDEO_NOTE -> Methods.sendVideoNote();
            case VOICE -> Methods.sendVoice();
        };
    }

    public static ActionType resolveAction(FileType fileType) {
        return switch (fileType) {
            case VIDEO -> ActionType.UPLOADVIDEO;
            case VIDEO_NOTE -> ActionType.UPLOADVIDEONOTE;
            case VOICE -> ActionType.UPLOADVOICE;
            default -> ActionType.TYPING;
        };
    }
}
