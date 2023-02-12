package com.github.manjago.nmm;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

public class ExceptionUtils {

    private ExceptionUtils() {
        // utility class hide constructor
    }

    // утащено с https://www.baeldung.com/java-exception-root-cause и слегка модифицировано
    public static @NotNull Throwable findRootCause(Throwable throwable) {
        Throwable rootCause = Objects.requireNonNull(throwable, "Throwable must be not null");
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    public static @NotNull String stackTraceToString(@NotNull Throwable throwable) {
        final var stringWriter = new StringWriter();
        final var printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
