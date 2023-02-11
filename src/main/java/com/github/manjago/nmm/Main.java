package com.github.manjago.nmm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        final var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();



        //xn--https://-b9g3bk1c.xn--/121/-uye2a
        final var httpRequest = HttpRequest.newBuilder(new URI("https://xn--h1ahcp.xn--p1ai/1216"))
                .GET()
                .build();

        final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse);
    }
}
