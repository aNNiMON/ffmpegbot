package com.annimon.ffmpegbot.session;

import com.annimon.ffmpegbot.parameters.InputParameters;

import java.util.StringJoiner;

public abstract sealed class Session permits MediaSession, YtDlpSession {
    // Session key
    protected long chatId;
    protected int messageId;
    // Parameters
    protected final InputParameters inputParams = new InputParameters();

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

    public InputParameters getInputParams() {
        return inputParams;
    }

    abstract StringJoiner describe();
}
