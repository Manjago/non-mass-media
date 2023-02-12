package com.github.manjago.nmm.impl;

import com.github.manjago.nmm.NextArticleIdProvider;
import com.github.manjago.nmm.NonMassMediaException;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NextArticleIdProviderImpl implements NextArticleIdProvider {

    private final @NotNull String pathToDataFile;

    public NextArticleIdProviderImpl(@NotNull String pathToDataFile) {
        this.pathToDataFile = pathToDataFile;
    }

    @Override
    public int nextArticleId() {
        try {
            final var textLastArticleId = new String(Files.readAllBytes(Paths.get(pathToDataFile)));
            return Integer.parseInt(textLastArticleId) + 1;
        } catch (Exception e) {
            throw new NonMassMediaException(e);
        }
    }

    @Override
    public void storeArticleId(int articleId) {
        try {
            Files.writeString(Paths.get(pathToDataFile), Integer.toString(articleId), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            throw new NonMassMediaException(e);
        }
    }
}
