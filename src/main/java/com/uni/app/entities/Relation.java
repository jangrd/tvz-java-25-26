package com.uni.app.entities;

import com.uni.app.exceptions.ValidationException;

import java.util.Objects;

/**
 * Represents an immutable, directed link between two {@link Identifiable} entities.
 *
 * <p>Used to pair two entities without a dedicated relationship class, for
 * example a student with a session. Equality is based on both ends of the
 * relation.</p>
 *
 * @param <A> the type of the left entity
 * @param <B> the type of the right entity
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public class Relation<A extends Identifiable, B extends Identifiable> {
    private final A left;
    private final B right;

    /**
     * Creates a relation between the two given entities.
     *
     * @param left  the left entity
     * @param right the right entity
     * @throws ValidationException if {@code left} or {@code right} is {@code null}
     */
    public Relation(A left, B right) {
        if (left == null) {
            throw new ValidationException("Relation 'left' must not be null");
        }
        if (right == null) {
            throw new ValidationException("Relation 'right' must not be null");
        }

        this.left = left;
        this.right = right;
    }

    /**
     * Compares this relation with another object for equality by both ends.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a relation with the same left and
     *         right entities, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Relation<?, ?> relation = (Relation<?, ?>) o;
        return Objects.equals(getLeft(), relation.getLeft()) && Objects.equals(getRight(), relation.getRight());
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code derived from the left and right entities
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getRight());
    }

    /**
     * Returns the left entity of this relation.
     *
     * @return the left entity
     */
    public A getLeft() {
        return left;
    }

    /**
     * Returns the right entity of this relation.
     *
     * @return the right entity
     */
    public B getRight() {
        return right;
    }
}
