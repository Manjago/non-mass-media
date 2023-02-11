package com.github.manjago.nmm;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker {

    private final static Logger logger = Logger.getLogger(Worker.class.getName());

    static {
        logger.setLevel(Level.INFO);
        logger.addHandler(new ConsoleHandler());
    }

    private final NextArticleIdProvider nextArticleIdProvider;
    private final ArticleUrlRetriever articleUrlRetriever;
    private final TelegramPoster telegramPoster;

    public Worker(NextArticleIdProvider nextArticleIdProvider,
                  ArticleUrlRetriever articleUrlRetriever,
                  TelegramPoster telegramPoster) {
        this.nextArticleIdProvider = nextArticleIdProvider;
        this.articleUrlRetriever = articleUrlRetriever;
        this.telegramPoster = telegramPoster;

    }

    public boolean run() {
        final var nextArticleId = nextArticleIdProvider.nextArticleId();
        final var articleUrl = articleUrlRetriever.retrieveUrl(nextArticleId);
        if (articleUrl == null) {
            return false;
        }

        nextArticleIdProvider.storeArticleId(nextArticleId);
        final var successPost = telegramPoster.postToTelegram(articleUrl);
        if (!successPost) {
            logger.log(Level.SEVERE, "Fail post article " + articleUrl);
        } else {
            logger.log(Level.INFO, "Success post article " + articleUrl);
        }

        return successPost;
    }
}
