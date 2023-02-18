package com.github.manjago.nmm.impl;

import com.github.manjago.nmm.NextArticleIdProvider;
import com.github.manjago.nmm.NonMassMediaException;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NextArticleIdProviderImpl implements NextArticleIdProvider {

    private final int depth;

    private final @NotNull String pathToDataFile;

    public NextArticleIdProviderImpl(int depth, @NotNull String pathToDataFile) {
        if (depth < 1) {
            throw new IllegalArgumentException("Bad depth value: " + depth);
        }
        this.depth = depth;
        this.pathToDataFile = pathToDataFile;
    }

    @Override
    public int[] nextArticleIds() {
        try {
            final var textLastArticleId = new String(Files.readAllBytes(Paths.get(pathToDataFile)));
            final int seed = Integer.parseInt(textLastArticleId) + 1;
            final int[] result = new int[depth];
            for(int i = 0; i < depth; ++i) {
               result[i] = seed + i;
            }
            return result;
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
