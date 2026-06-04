package com.uni.app.enums;

/**
 * Enumerates the possible attendance statuses of a student at a session.
 *
 * <p>Each constant carries a human-readable name intended for display in the
 * user interface, while the constant itself is used throughout the code.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public enum AttendanceStatus {
    /** The student was absent. */
    ABSENT("Odsutan"),
    /** The student was present. */
    PRESENT("Prisutan"),
    /** The student's absence was excused. */
    EXCUSED("Isprican");

    private final String displayName;

    AttendanceStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable name of this attendance status.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
