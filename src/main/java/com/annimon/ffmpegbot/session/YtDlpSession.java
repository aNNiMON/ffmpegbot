package com.annimon.ffmpegbot.session;

import java.util.StringJoiner;

public final class YtDlpSession extends Session {
    private final String url;
    private final String downloadOption;
    private final FileType fileType;
    private String outputFilename;

    public YtDlpSession(String url, String downloadOption, FileType fileType) {
        this.url = url;
        this.downloadOption = downloadOption;
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public String getDownloadOption() {
        return downloadOption;
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    @Override
    public StringJoiner describe() {
        final var joiner = new StringJoiner("\n");
        joiner.add("URL: <code>%s</code>".formatted(url));
        joiner.add("Type: <code>%s</code>".formatted(fileType));
        joiner.merge(inputParams.describe());
        return joiner;
    }

    @Override
    public String toString() {
        return describe().toString();
    }
}
