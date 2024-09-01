FROM gradle:8.1.1-jdk17-alpine AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN GRADLE_OPTS="-Xmx256m" gradle build --build-cache --stacktrace -i --no-daemon

FROM gradle:8.1.1-jdk17-alpine as builder
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/java-code
WORKDIR /usr/src/java-code
RUN GRADLE_OPTS="-Xmx256m" gradle shadowJar --build-cache --stacktrace --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache python3 python3-dev py3-pip ffmpeg gcc g++ \
  && python3 -m pip install --break-system-packages --upgrade wheel yt-dlp pyrogram TgCrypto
WORKDIR /app
COPY --from=builder /usr/src/java-code/build/libs/ffmpegbot-1.2-SNAPSHOT-all.jar .
RUN mkdir input && mkdir output
COPY pytgfile.py .
COPY ffmpegbot-docker.yaml .
ENTRYPOINT ["java", "-jar", "/app/ffmpegbot-1.2-SNAPSHOT-all.jar", "docker"]
