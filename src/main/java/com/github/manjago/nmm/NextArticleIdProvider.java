package com.github.manjago.nmm;

public interface NextArticleIdProvider {
    int[] nextArticleIds();
    void storeArticleId(int articleId);
}
