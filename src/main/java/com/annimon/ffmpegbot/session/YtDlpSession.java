package com.annimon.ffmpegbot.session;

public class YtDlpSession {
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
}
