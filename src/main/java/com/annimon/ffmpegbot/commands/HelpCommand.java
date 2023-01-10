package com.annimon.ffmpegbot.commands;

import com.annimon.tgbotsmodule.commands.TextCommand;
import com.annimon.tgbotsmodule.commands.authority.For;
import com.annimon.tgbotsmodule.commands.context.MessageContext;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public class HelpCommand implements TextCommand {

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public Set<String> aliases() {
        return Set.of("/start");
    }

    @SuppressWarnings("unchecked")
    @Override
    public EnumSet<For> authority() {
        return For.all();
    }

    @Override
    public void accept(@NotNull MessageContext ctx) {
        ctx.replyToMessage("""
                <b>Media processing</b>
                Send any media to start processing.
                
                <b>Input parameters</b> (in reply to media processing message)
                /ss — set media start position
                /to — set media end position
                /t — set media duration
                
                <b>yt-dlp</b>
                /dl link [format] — download a media using yt-dlp
                  `link` — a link to download (it must be supported by yt-dlp)
                  `format` — (optional) a download format. Can be "audio", "240", "360", "480", "720" or "1080"
                """.stripIndent()).enableHtml().callAsync(ctx.sender);
    }
}
