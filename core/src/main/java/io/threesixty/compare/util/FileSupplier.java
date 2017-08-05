package io.threesixty.compare.util;

import java.io.File;
import java.net.URISyntaxException;

public class FileSupplier {
    public static File forResource(final String resource) throws URISyntaxException {
        return new File(FileSupplier.class.getClass().getResource(resource).toURI());
    }
}
