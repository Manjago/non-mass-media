package com.github.manjago.nmm;

public interface NextArticleIdProvider {
    int nextArticleId();
    void storeArticleId(int articleId);
}
