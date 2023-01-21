package com.annimon.ffmpegbot.session;

import com.annimon.ffmpegbot.parameters.InputParameters;
import com.annimon.ffmpegbot.parameters.Parameter;

import java.io.File;
import java.util.List;
import java.util.StringJoiner;

import static com.annimon.ffmpegbot.TextUtils.*;

public class MediaSession {
    // Session key
    private long chatId;
    private int messageId;
    // Media info
    private FileType fileType;
    private String fileId;
    private String originalFilename;
    private Long fileSize;
    private Integer duration;
    private String dimensions;
    // Parameters
    private List<Parameter<?>> params;
    private final InputParameters inputParams = new InputParameters();
    // Files
    private File inputFile;
    private File outputFile;
    // Status
    private String status;

    public void fromFileInfo(FileInfo fileInfo) {
        this.setFileId(fileInfo.fileId());
        this.setFileType(fileInfo.fileType());
        this.setFileSize(fileInfo.fileSize());
        this.setDuration(fileInfo.duration());
        this.setDimensions(fileInfo.width(), fileInfo.height());
        this.setOriginalFilename(fileInfo.filename());
    }

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

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setDimensions(Integer width, Integer height) {
        if (width == null && height == null) {
            this.dimensions = null;
        } else {
            this.dimensions = (width != null ? width : "?") + "x" + (height != null ? height : "?");
        }
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

    public void setStatus(String status) {
        this.status = status;
    }

    public StringJoiner describe() {
        final var joiner = new StringJoiner("\n");
        joiner.add("Type: <code>%s</code>".formatted(fileType));
        if (fileSize != null && fileSize > 0) {
            joiner.add("File size: <code>%s</code>".formatted(readableFileSize(fileSize)));
        }
        if (duration != null && duration > 0) {
            joiner.add("Duration: <code>%s</code>".formatted(readableDuration(duration)));
        }
        if (dimensions != null) {
            joiner.add("Dimensions: <code>%s</code>".formatted(dimensions));
        }
        joiner.merge(inputParams.describe());
        if (originalFilename != null) {
            joiner.add("Filename: <code>%s</code>".formatted(safeHtml(originalFilename)));
        }
        if (inputFile != null && inputFile.canRead()) {
            joiner.add("Input file: <code>%s</code>".formatted(safeHtml(inputFile.getName())));
            joiner.add("Input Size: <code>%s</code>".formatted(readableFileSize(inputFile.length())));
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
