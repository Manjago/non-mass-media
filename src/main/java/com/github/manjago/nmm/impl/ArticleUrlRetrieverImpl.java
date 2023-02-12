package com.github.manjago.nmm.impl;

import com.github.manjago.nmm.ArticleUrlRetriever;
import com.github.manjago.nmm.NonMassMediaException;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ArticleUrlRetrieverImpl implements ArticleUrlRetriever {

    private final String baseUrl;
    private final HttpClient httpClient;

    public ArticleUrlRetrieverImpl(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    @Override
    public @Nullable String retrieveUrl(int articleId) {

        try {
            final String pretender = "%s%d".formatted(baseUrl, articleId);

            final var httpRequest = HttpRequest.newBuilder(new URI("https://xn--h1ahcp.xn--p1ai/1216"))
                    .GET()
                    .build();

            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() != 404) {
                return pretender;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new NonMassMediaException(e);
        }


    }
}
