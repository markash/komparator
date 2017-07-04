package io.threesixty.kt.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple4;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class Difference<T> extends Tuple4<String, T, T, Boolean> {
    /**
     *
     * @param left The left id
     * @param right The right id
     */
    public Difference(final Id2<T> left, final Id2<T> right) {
        this(
                left.getName(),
                left.getValue(),
                right != null ? right.getValue() : null,
                !EqualsBuilder.reflectionEquals(left.getValue(), right != null ? right.getValue() : null));
    }
    /**
     *
     * @param difference The attribute difference
     */
    public Difference(final Tuple2<Attribute<T>, Attribute<T>> difference) {
        this(
                difference.v1.getName(),
                difference.v1.getValue(),
                difference.v2.getValue(),
                !EqualsBuilder.reflectionEquals(difference.v1.getValue(), difference.v2.getValue()));
    }
    /**
     *
     * @param left The left attribute
     * @param right The right attribute
     */
    public Difference(final Attribute<T> left, final Attribute<T> right) {
        this(
                left.getName(),
                left.getValue(),
                right.getValue(),
                !EqualsBuilder.reflectionEquals(left.getValue(), right.getValue()));
    }
    /**
     *
     * @param name The name of the attribute
     * @param leftValue The left hand value of the equality
     * @param rightValue The right hand value of the equality
     * @param different Whether the left hand value is different from the right hand value
     */
    public Difference(String name, T leftValue, T rightValue, boolean different) {
        super(name, leftValue, rightValue, different);
    }

    /**
     * The name of the attribute which is the v1 value of the Tuple
     * @return The name of the attribute
     */
    public String getName() { return v1; }
    /**
     * The left value of the attribute which is the v2 value of the Tuple
     * @return The left value of the attribute
     */
    public T getLeftValue() { return v2; }
    /**
     * The right value of the attribute which is the v2 value of the Tuple
     * @return The right value of the attribute
     */
    public T getRightValue() { return v3; }
    /**
     * The value of the attribute which is the v2 value of the Tuple
     * @return Whether the left and right values are different
     */
    public boolean isDifferent() { return v4; }

    @Override
    public String toString() {
        return getLeftValue() + (isDifferent() ? " (" + getRightValue() + ") " : "");
    }
}