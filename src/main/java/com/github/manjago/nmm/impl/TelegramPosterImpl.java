package com.github.manjago.nmm.impl;

import com.github.manjago.nmm.NonMassMediaException;
import com.github.manjago.nmm.TelegramPoster;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TelegramPosterImpl implements TelegramPoster {

    private final HttpClient httpClient;
    private final String botToken;
    private final String channelId;
    private final String adminId;

    public TelegramPosterImpl(HttpClient httpClient, String botToken, String channelId, String adminId) {
        this.httpClient = httpClient;
        this.botToken = botToken;
        this.channelId = channelId;
        this.adminId = adminId;
    }

    @Override
    public void postToTelegramChannel(@NotNull String text) {
        post(channelId, text);
    }

    @Override
    public void postToAdmin(@NotNull String text) {
        post(adminId, text);
    }

    private void post(@NotNull String chatId, @NotNull String text) {
        try {

            final String jsonBody = "{\"chat_id\":\"%s\",\"text\":\"%s\"}".formatted(chatId, text);

            final var httpRequest = HttpRequest.newBuilder(new URI("https://api.telegram.org/bot%s/sendMessage".formatted(botToken)))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            final var httpResponse = httpClient.send(
                    httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() != 200) {
                throw new NonMassMediaException("Bad response %d %s".formatted(httpResponse.statusCode(), httpResponse.body()));
            }

        } catch (NonMassMediaException e) {
            throw e;
        }catch (Exception e) {
            throw new NonMassMediaException(e);
        }
    }
}
