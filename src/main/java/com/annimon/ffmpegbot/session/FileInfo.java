package com.annimon.ffmpegbot.session;

import org.apache.commons.io.FilenameUtils;
import java.util.Locale;

public record FileInfo(FileType fileType, String fileId, String filename,
                       Long fileSize, Integer duration, Integer width, Integer height) {
    public FileInfo(FileType fileType, String fileId, String filename, Long fileSize, Integer duration) {
        this(fileType, fileId, filename, fileSize, duration, null, null);
    }

    public String getExtension() {
        return FilenameUtils.getExtension(filename).toLowerCase(Locale.ROOT);
    }
}
