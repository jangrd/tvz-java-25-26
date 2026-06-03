package com.uni.app.enums;

/**
 * Enumerates the study programmes a student can be enrolled in.
 *
 * <p>Each constant carries a human-readable name intended for display in the
 * user interface, while the constant itself is used throughout the code.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public enum StudyProgramme {
    /** Computing study programme. */
    COMPUTING("Računarstvo"),
    /** Electrical engineering study programme. */
    ELECTRICAL_ENGINEERING("Elektrotehnika"),
    /** Mechanical engineering study programme. */
    MECHANICAL_ENGINEERING("Strojarstvo"),
    /** Civil engineering study programme. */
    CIVIL_ENGINEERING("Graditeljstvo"),
    /** Informatics study programme. */
    INFORMATICS("Informatika");

    private final String displayName;

    StudyProgramme(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the human-readable name of this study programme.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
