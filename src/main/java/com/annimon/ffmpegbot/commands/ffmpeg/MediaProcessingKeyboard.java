package com.annimon.ffmpegbot.commands.ffmpeg;

import com.annimon.ffmpegbot.parameters.Parameter;
import com.annimon.ffmpegbot.session.MediaSession;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.annimon.ffmpegbot.commands.ffmpeg.CallbackQueryCommands.*;

public class MediaProcessingKeyboard {
    public static InlineKeyboardMarkup createKeyboard(MediaSession mediaSession) {
        final var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        for (Parameter<?> param : mediaSession.getParams()) {
            final String paramId = param.getId();
            keyboard.add(List.of(
                    inlineKeyboardButton("<", callbackData(PREV, paramId)),
                    inlineKeyboardButton(param.describe(), callbackData(DETAIL, paramId)),
                    inlineKeyboardButton(">", callbackData(NEXT, paramId))
            ));
        }
        keyboard.add(List.of(inlineKeyboardButton("Process", callbackData(PROCESS))));
        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardButton inlineKeyboardButton(String text, String callbackData) {
        final var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    @SuppressWarnings("SameParameterValue")
    private static String callbackData(String command) {
        return command;
    }

    private static String callbackData(String command, String data) {
        return command + ":" + data;
    }
}