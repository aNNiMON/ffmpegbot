# ffmpegbot

Telegram Bot for re-encoding media

## Features

 - change a video resolution, bitrate, frame rate, speed
 - change an audio bitrate, volume, pitch
 - apply audio effects
 - extract or remove audio from the video
 - resend video note as a regular video, or video as an audio track

## Requirements

 - Telegram bot username and token, [@BotFather](https://t.me/BotFather)
 - JRE 17+ or JDK 17+ (for build)
 - `ffmpeg` must be installed and available in `PATH`.
 - `python3` version 3.8+ must be installed and available in `PATH`.
 - `yt-dlp` for `/dl` command.

## Installation

```bash
# Build
./gradlew shadowJar
# Config
cp ffmpegbot.yaml.template ffmpegbot.yaml
vim ffmpegbot.yaml
mkdir {input,output}
# Run
java -jar ./build/libs/ffmpegbot-1.0-SNAPSHOT-all.jar
```
