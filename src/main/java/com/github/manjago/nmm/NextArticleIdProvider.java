package com.github.manjago.nmm;

public interface NextArticleIdProvider {
    int nextArticleId();
    boolean storeArticleId(int articleId);
}
