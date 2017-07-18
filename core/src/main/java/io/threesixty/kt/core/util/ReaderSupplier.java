package io.threesixty.kt.core.util;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Supplier;

public class ReaderSupplier  {

    public static Reader forResource(final String resource) {
        return new InputStreamReader(ReaderSupplier.class.getClass().getResourceAsStream(resource));
    }
}
