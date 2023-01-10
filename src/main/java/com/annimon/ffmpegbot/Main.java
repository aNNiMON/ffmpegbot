package com.annimon.ffmpegbot;

import com.annimon.tgbotsmodule.BotHandler;
import com.annimon.tgbotsmodule.BotModule;
import com.annimon.tgbotsmodule.Runner;
import com.annimon.tgbotsmodule.beans.Config;
import com.annimon.tgbotsmodule.services.YamlConfigLoaderService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Main implements BotModule {
    public static void main(String[] args) {
        final var profile = (args.length >= 1 && !args[0].isEmpty()) ? args[0] : "";
        Runner.run(profile, List.of(new Main()));
    }

    @Override
    public @NotNull BotHandler botHandler(@NotNull Config config) {
        final var configLoader = new YamlConfigLoaderService();
        final var configFile = configLoader.configFile("ffmpegbot", config.getProfile());
        final var wordlyConfig = configLoader.loadFile(configFile, BotConfig.class);
        return new MainBotHandler(wordlyConfig);
    }
}
