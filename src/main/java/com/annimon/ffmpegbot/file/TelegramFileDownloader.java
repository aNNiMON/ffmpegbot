package com.annimon.ffmpegbot.file;

import com.annimon.ffmpegbot.TelegramRuntimeException;
import com.annimon.tgbotsmodule.api.methods.Methods;
import com.annimon.tgbotsmodule.services.CommonAbsSender;
import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;

public class TelegramFileDownloader implements FileDownloader {

    @Override
    public File downloadFile(CommonAbsSender sender, String fileId, String defaultFilename) {
        try {
            final var tgFile = Methods.getFile(fileId).call(sender);
            final var extension = FilenameUtils.getExtension(tgFile.getFilePath());
            return sender.downloadFile(tgFile, File.createTempFile("tmp", "file." + extension));
        } catch (IOException | TelegramApiException | TelegramRuntimeException e) {
            throw new FileDownloadException(e.getMessage(), e);
        }
    }
}
