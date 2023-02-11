package com.github.manjago.nmm;

import org.jetbrains.annotations.NotNull;

public interface TelegramPoster {
    boolean postToTelegram(@NotNull String text);
}
