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

### Manual

```bash
# Build
./gradlew shadowJar
# Config
cp ffmpegbot.yaml.template ffmpegbot.yaml
vim ffmpegbot.yaml
mkdir {input,output}
# Run
java -jar ./build/libs/ffmpegbot-1.1-SNAPSHOT-all.jar
```


### Using Docker

Note: FFmpeg binary might be installed with limited number of filters and codecs. Some bot features might not work (Audio pitch, robot effect, etc).

```bash
# Edit user ids in `superUsers` and `allowedUsers` fields
vim ffmpegbot-docker.yaml
docker build --tag 'ffmpegbot' .
docker run -d -t -i \
  -e BOT_TOKEN='...' \
  -e BOT_USERNAME='...' \
  -e APP_ID='...' \
  -e APP_HASH='...'\
  --name ffmpegbot ffmpegbot:latest
```

#### Environment variables

 - `BOT_TOKEN` — Telegram bot token
 - `BOT_USERNAME` — Telegram bot username
 - `APP_ID` — Telegram API app_id (see https://core.telegram.org/api/obtaining_api_id)
 - `APP_HASH` — Telegram API app_hash

