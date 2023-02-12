package com.github.manjago.nmm;

import java.time.Duration;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.manjago.nmm.ExceptionUtils.findRootCause;

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

    public void run(Duration sleepInterval) {
        try {
             while(oneStep()) {
                 Thread.sleep(sleepInterval.toMillis());
             }
        } catch (Exception e) {
            final var exceptionMessage = "Exception happens: %s".formatted(findRootCause(e).getMessage());
            logger.log(Level.SEVERE, exceptionMessage, e);
        }
    }

    boolean oneStep() {
        final var nextArticleId = nextArticleIdProvider.nextArticleId();
        final var articleUrl = articleUrlRetriever.retrieveUrl(nextArticleId);
        if (articleUrl == null) {
            return false;
        }

        nextArticleIdProvider.storeArticleId(nextArticleId);
        try {
            telegramPoster.postToTelegramChannel(articleUrl);
        } catch (Exception e) {
            final var exceptionMessage = "Fail post article %s: %s".formatted(articleUrl, findRootCause(e).getMessage());
            logger.log(Level.SEVERE, exceptionMessage, e);
            throw new NonMassMediaException(exceptionMessage, e);
        }
        return true;
    }
}
