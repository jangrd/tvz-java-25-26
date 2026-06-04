package com.uni.app.entities;

import com.uni.app.enums.StudyProgramme;
import com.uni.app.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a student enrolled in a study programme, identified by their JMBAG.
 *
 * <p>Extends {@link Person} with the JMBAG (the identity), the current year of
 * study and the study programme. Instances are created through {@link Builder};
 * the JMBAG is validated with the Luhn algorithm when a student is built.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Person
 * @since 1.0
 */
public final class Student extends Person {
    // Duration of the longest study programme, in years
    private static final int MAX_YEAR_OF_STUDY = 6;

    private final String jmbag;
    private int yearOfStudy;
    private StudyProgramme studyProgramme;

    /**
     * Creates a student from the supplied builder, validating the JMBAG and the
     * year of study.
     *
     * @param builder the builder holding the student's data
     * @throws ValidationException if the JMBAG is invalid, the year of study is
     *                             outside the allowed range, or the study
     *                             programme is {@code null}
     */
    private Student(Builder builder) {
        super(builder.oib, builder.firstName, builder.lastName, builder.email, builder.dob);
        if (!isValidJmbag(builder.jmbag)) {
            throw new ValidationException("Student 'jmbag' not valid");
        }
        if (builder.yearOfStudy <= 0 || builder.yearOfStudy > MAX_YEAR_OF_STUDY) {
            throw new ValidationException("Student 'yearOfStudy' out of range. [1, " + MAX_YEAR_OF_STUDY + "]");
        }
        if (builder.studyProgramme == null) {
            throw new ValidationException("Student 'studyProgramme' must not be null");
        }

        this.jmbag = builder.jmbag;
        this.yearOfStudy = builder.yearOfStudy;
        this.studyProgramme = builder.studyProgramme;
    }

    /**
     * Returns the student's identity, which is the JMBAG.
     *
     * @return the JMBAG
     */
    @Override
    public String getId() {
        return jmbag;
    }

    /**
     * Compares this student with another object for equality by their JMBAG.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a student with the same JMBAG,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(getId(), ((Student) o).getId());
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code derived from the JMBAG
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /**
     * Returns the student's JMBAG.
     *
     * @return the immutable JMBAG
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Returns the student's current year of study.
     *
     * @return the year of study
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * Sets the student's current year of study.
     *
     * @param yearOfStudy the new year of study (1 to {@value #MAX_YEAR_OF_STUDY})
     * @throws ValidationException if the year of study is outside the allowed range
     */
    public void setYearOfStudy(int yearOfStudy) {
        if (yearOfStudy <= 0 || yearOfStudy > MAX_YEAR_OF_STUDY) {
            throw new ValidationException("Student 'yearOfStudy' out of range. [1, " + MAX_YEAR_OF_STUDY + "]");
        }
        this.yearOfStudy = yearOfStudy;
    }

    /**
     * Returns the student's study programme.
     *
     * @return the study programme
     */
    public StudyProgramme getStudyProgramme() {
        return studyProgramme;
    }

    /**
     * Sets the student's study programme.
     *
     * @param studyProgramme the new study programme
     * @throws ValidationException if {@code studyProgramme} is {@code null}
     */
    public void setStudyProgramme(StudyProgramme studyProgramme) {
        if (studyProgramme == null) {
            throw new ValidationException("Student 'studyProgramme' must not be null");
        }
        this.studyProgramme = studyProgramme;
    }

    /**
     * Validates the provided JMBAG.
     *
     * <p>Returns {@code true} only when all of the following hold:</p>
     * <ul>
     *     <li>the value is not {@code null};</li>
     *     <li>it is exactly 10 characters long;</li>
     *     <li>every character is a digit;</li>
     *     <li>it passes the Luhn (MOD 10) check, whose last digit is the control digit.</li>
     * </ul>
     *
     * @param jmbag the JMBAG to validate
     * @return {@code true} if {@code jmbag} is a valid JMBAG, {@code false} otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Luhn_algorithm">Luhn algorithm</a>
     */
    private static boolean isValidJmbag(String jmbag) {
        if (jmbag == null) {
            return false;
        }
        if (jmbag.length() != 10) {
            return false;
        }
        if (!jmbag.chars().allMatch(Character::isDigit)) {
            return false;
        }
        int sum = 0;
        boolean doubleIt = false;
        for (char c : new StringBuilder(jmbag).reverse().toString().toCharArray()) {
            int d = c - '0';
            if (doubleIt) {
                d *= 2;
                if (d > 9) {
                    d -= 9;
                }
            }
            sum += d;
            doubleIt = !doubleIt;
        }
        return sum % 10 == 0;
    }

    /**
     * Builds {@link Student} instances using a fluent API.
     *
     * <p>Set the desired fields with the chained methods, then call
     * {@link #build()}. Validation of the JMBAG and year of study happens when
     * the student is built.</p>
     *
     * @author Jan Grdanjski
     * @version 1.0
     * @since 1.0
     */
    public static class Builder {
        private String oib;
        private String firstName;
        private String lastName;
        private String email;
        private LocalDate dob;
        private String jmbag;
        private int yearOfStudy;
        private StudyProgramme studyProgramme;

        /**
         * Creates an empty builder.
         */
        public Builder() {
        }

        /**
         * Sets the OIB.
         *
         * @param oib the OIB
         * @return this builder
         */
        public Builder oib(String oib) {
            this.oib = oib;
            return this;
        }

        /**
         * Sets the first name.
         *
         * @param firstName the first name
         * @return this builder
         */
        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
         * Sets the last name.
         *
         * @param lastName the last name
         * @return this builder
         */
        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Sets the email address.
         *
         * @param email the email address
         * @return this builder
         */
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the date of birth.
         *
         * @param dob the date of birth
         * @return this builder
         */
        public Builder dob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        /**
         * Sets the JMBAG.
         *
         * @param jmbag the 10-digit JMBAG
         * @return this builder
         */
        public Builder jmbag(String jmbag) {
            this.jmbag = jmbag;
            return this;
        }

        /**
         * Sets the year of study.
         *
         * @param yearOfStudy the current year of study
         * @return this builder
         */
        public Builder yearOfStudy(int yearOfStudy) {
            this.yearOfStudy = yearOfStudy;
            return this;
        }

        /**
         * Sets the study programme.
         *
         * @param studyProgramme the study programme
         * @return this builder
         */
        public Builder studyProgramme(StudyProgramme studyProgramme) {
            this.studyProgramme = studyProgramme;
            return this;
        }

        /**
         * Builds a validated {@link Student} from the current values.
         *
         * @return a new {@link Student}
         * @throws ValidationException if the supplied data is invalid
         */
        public Student build() {
            return new Student(this);
        }
    }
}
