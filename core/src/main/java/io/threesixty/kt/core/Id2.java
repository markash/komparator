package io.threesixty.kt.core;

import org.jooq.lambda.tuple.Tuple2;

/**
 * @author Mark P Ashworth
 */
public class Id2<T> extends Tuple2<String, T> {

    public Id2(String name, T value) {
        super(name, value);
    }

    /**
     * The name of the id which is the v1 value of the Tuple
     * @return The name of the id
     */
    public String getName() { return v1; }
    /**
     * The value of the id which is the v2 value of the Tuple
     * @return The value of the id
     */
    public T getValue() { return v2; }
}