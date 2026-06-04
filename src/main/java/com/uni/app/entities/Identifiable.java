package com.uni.app.entities;

/**
 * Marks a type that exposes a unique identity used for equality, hashing and
 * persistence.
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public sealed interface Identifiable permits Person, Course, Session, Attendance {
    /**
     * Returns the unique identity of this object.
     *
     * @return the identity
     */
    String getId();
}
