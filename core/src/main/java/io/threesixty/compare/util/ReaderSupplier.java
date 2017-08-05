package io.threesixty.compare.util;

import java.io.InputStreamReader;
import java.io.Reader;

public class ReaderSupplier  {

    public static Reader forResource(final String resource) {
        return new InputStreamReader(ReaderSupplier.class.getClass().getResourceAsStream(resource));
    }
}
