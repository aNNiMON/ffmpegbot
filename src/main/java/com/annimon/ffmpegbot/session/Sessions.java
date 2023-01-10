package com.annimon.ffmpegbot.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Sessions {
    private final Map<String, MediaSession> sessions;

    public Sessions() {
        sessions = new ConcurrentHashMap<>();
    }

    public MediaSession get(long chatId, long messageId) {
        return sessions.get(mapKey(chatId, messageId));
    }

    public void put(MediaSession mediaSession) {
        sessions.put(mapKey(mediaSession.getChatId(), mediaSession.getMessageId()), mediaSession);
    }

    public void put(long chatId, long messageId, MediaSession mediaSession) {
        sessions.put(mapKey(chatId, messageId), mediaSession);
    }

    private String mapKey(long chatId, long messageId) {
        return chatId + "/" + messageId;
    }
}
