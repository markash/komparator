package io.threesixty.kt.core.util;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.function.Supplier;

public class FileSupplier {
    public static File forResource(final String resource) throws URISyntaxException {
        return new File(FileSupplier.class.getClass().getResource(resource).toURI());
    }
}
