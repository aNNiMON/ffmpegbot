package com.annimon.ffmpegbot.session;

import com.annimon.ffmpegbot.parameters.InputParameters;
import com.annimon.ffmpegbot.parameters.Parameter;

import java.io.File;
import java.util.List;
import java.util.StringJoiner;

import static com.annimon.ffmpegbot.TextUtils.readableFileSize;
import static com.annimon.ffmpegbot.TextUtils.safeHtml;

public class MediaSession {
    // Session key
    private long chatId;
    private int messageId;
    // Media info
    private FileType fileType;
    private String fileId;
    private String originalFilename;
    // Parameters
    private List<Parameter<?>> params;
    private final InputParameters inputParams = new InputParameters();
    // Files
    private File inputFile;
    private File outputFile;
    // Status
    private String status;

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public List<Parameter<?>> getParams() {
        return params;
    }

    public InputParameters getInputParams() {
        return inputParams;
    }

    public void setParams(List<Parameter<?>> params) {
        this.params = params;
    }

    public boolean isDownloaded() {
        return (inputFile != null) && (inputFile.canRead());
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StringJoiner describe() {
        final var joiner = new StringJoiner("\n");
        joiner.add("File ID: <code>%s</code>".formatted(safeHtml(fileId)));
        joiner.add("Type: <code>%s</code>".formatted(fileType));
        joiner.merge(inputParams.describe());
        if (originalFilename != null) {
            joiner.add("Filename: <code>%s</code>".formatted(safeHtml(originalFilename)));
        }
        if (inputFile != null && inputFile.canRead()) {
            joiner.add("Input file: <code>%s</code>".formatted(safeHtml(inputFile.getName())));
            joiner.add("Size: <code>%s</code>".formatted(readableFileSize(inputFile.length())));
        }
        if (outputFile != null && outputFile.canRead()) {
            joiner.add("Output size: <code>%s</code>".formatted(readableFileSize(outputFile.length())));
        }
        if (status != null) {
            joiner.add("<i>" + safeHtml(status) + "</i>");
        }
        return joiner;
    }

    @Override
    public String toString() {
        return describe().toString();
    }
}
