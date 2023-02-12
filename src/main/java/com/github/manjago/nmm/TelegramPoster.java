package com.github.manjago.nmm;

import org.jetbrains.annotations.NotNull;

public interface TelegramPoster {
    void postToTelegramChannel(@NotNull String text);
    void postToAdmin(@NotNull String text);
}
