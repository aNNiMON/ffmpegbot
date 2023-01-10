package com.annimon.ffmpegbot.parameters;

import java.util.StringJoiner;

public class InputParameters {
    private String startPosition = "";
    private String duration = "";
    private String endPosition = "";

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String ss) {
        this.startPosition = ss;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        this.endPosition = "";
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
        this.duration = "";
    }

    public StringJoiner describe() {
        final var joiner = new StringJoiner("\n");
        if (!startPosition.isEmpty()) {
            joiner.add("Start: <code>%s</code>".formatted(startPosition));
        }
        if (!endPosition.isEmpty()) {
            joiner.add("End: <code>%s</code>".formatted(endPosition));
        }
        if (!duration.isEmpty()) {
            joiner.add("Duration: <code>%s</code>".formatted(duration));
        }
        return joiner;
    }
}
