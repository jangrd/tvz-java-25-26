package com.uni.app.entities;

import java.time.LocalDateTime;

/**
 * Marks a type that takes place at a specific date and time.
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public interface Schedulable {
    /**
     * Returns the date and time at which this takes place.
     *
     * @return the date and time
     */
    LocalDateTime getDateTime();
}
