package com.annimon.ffmpegbot.commands.admin;

import com.annimon.tgbotsmodule.commands.CommandBundle;
import com.annimon.tgbotsmodule.commands.CommandRegistry;
import com.annimon.tgbotsmodule.commands.authority.For;
import org.jetbrains.annotations.NotNull;

public class AdminCommandBundle implements CommandBundle<For> {

    @Override
    public void register(@NotNull CommandRegistry commands) {
        commands.register(new RunCommand());
        commands.register(new ClearCommand());
    }
}
