package com.github.manjago.nmm;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.manjago.nmm.ExceptionUtils.findRootCause;

public class Worker {

    private final static Logger logger = Logger.getLogger(Worker.class.getName());

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

    public void run(Long sleepInterval, Integer maxAttempts) {
        try {
            int counter = 0;
            while (oneStep()) {
                ++counter;
                if (counter >= maxAttempts) {
                    break;
                }
                Thread.sleep(sleepInterval);
            }
        } catch (Exception e) {
            final var exceptionMessage = "Exception happens: %s".formatted(findRootCause(e).getMessage());
            logger.log(Level.SEVERE, exceptionMessage, e);
            telegramPoster.postToAdmin(ExceptionUtils.stackTraceToString(e));
        }
    }

    boolean oneStep() {
        final int[] nextArticleIds = nextArticleIdProvider.nextArticleIds();

        for (final int nextArticleId : nextArticleIds) {
            final var articleUrl = articleUrlRetriever.retrieveUrl(nextArticleId);
            if (articleUrl == null) {
                continue;
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

        return false;
    }
}
