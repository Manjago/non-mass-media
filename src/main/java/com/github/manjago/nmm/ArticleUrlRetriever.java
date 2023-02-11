package com.github.manjago.nmm;

import org.jetbrains.annotations.Nullable;

public interface ArticleUrlRetriever {
    @Nullable String retrieveUrl(int articleId);
}
