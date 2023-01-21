package com.annimon.ffmpegbot.session;

public record FileInfo(FileType fileType, String fileId, String filename,
                       Long fileSize, Integer duration, Integer width, Integer height) {
    public FileInfo(FileType fileType, String fileId, String filename, Long fileSize, Integer duration) {
        this(fileType, fileId, filename, fileSize, duration, null, null);
    }
}
