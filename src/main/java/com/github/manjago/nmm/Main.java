package com.github.manjago.nmm;

import com.github.manjago.nmm.impl.ArticleUrlRetrieverImpl;
import com.github.manjago.nmm.impl.NextArticleIdProviderImpl;
import com.github.manjago.nmm.impl.TelegramPosterImpl;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.net.http.HttpClient;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private final static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String @NotNull [] args) {


        if (args.length < 1) {
            logger.severe("Need properties file in argument");
            return;
        }
        logger.info("Loading properties from %s".formatted(args[0]));

        final var config = new Properties();
        try (final var is = new FileInputStream(args[0])) {
            config.load(is);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Fail loading properties from %s".formatted(args[0]), e);
            return;
        }
        final var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        final ArticleUrlRetriever articleUrlRetriever = new ArticleUrlRetrieverImpl(
                config.getProperty("article.url.retriever.base.url", "https://xn--h1ahcp.xn--p1ai/"),
                httpClient);

        final NextArticleIdProvider nextArticleIdProvider = new NextArticleIdProviderImpl(
                Integer.parseInt(config.getProperty("next.article.id.provider.data.depth", "5")),
                config.getProperty("next.article.id.provider.data.file")
        );

        final TelegramPoster telegramPoster = new TelegramPosterImpl(httpClient,
                config.getProperty("telegram.poster.bot.token"),
                config.getProperty("telegram.poster.bot.channel.id"),
                config.getProperty("telegram.poster.bot.admin.id")
        );

        final Worker worker = new Worker(
                nextArticleIdProvider, articleUrlRetriever, telegramPoster
        );

        final var sleepInterval = Long.parseLong(config.getProperty("worker.sleep.interval"));
        final var maxAttempts = Integer.parseInt(config.getProperty("worker.max.attempts", "10"));

        worker.run(sleepInterval, maxAttempts);
    }
}
