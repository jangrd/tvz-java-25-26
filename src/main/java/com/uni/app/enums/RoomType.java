package com.uni.app.enums;

/**
 * Enumerates the types of a room.
 *
 * <p>Each constant carries a human-readable name intended for display in the
 * user interface, while the constant itself is used throughout the code.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public enum RoomType {
    /** Lecture hall. */
    LECTURE_HALL("Predavaona"),
    /** Laboratory. */
    LABORATORY("Laboratorij"),
    /** Office. */
    OFFICE("Ured");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable name of this room type.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
