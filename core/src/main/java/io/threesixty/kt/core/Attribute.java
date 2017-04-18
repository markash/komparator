package io.threesixty.kt.core;

import org.jooq.lambda.tuple.Tuple2;

/**
 * @author Mark P Ashworth
 */
public class Attribute<T> extends Tuple2<String, T> {
    public Attribute(String v1, T v2) {
        super(v1, v2);
    }

    /**
     * The name of the attribute which is the v1 value of the Tuple
     * @return The name of the attribute
     */
    public String getName() { return v1; }
    /**
     * The value of the attribute which is the v2 value of the Tuple
     * @return The value of the attribute
     */
    public T getValue() { return v2; }
}