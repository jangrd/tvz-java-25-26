package com.uni.app.entities;

import com.uni.app.exceptions.ValidationException;

import java.util.Objects;

/**
 * Represents a course (kolegij), identified by its code.
 *
 * <p>Holds the course code (the identity), name, ECTS credits and the semester
 * in which it is taught. Equality and hashing are based on the code.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Identifiable
 * @since 1.0
 */
public final class Course implements Identifiable {
    private static final int MAX_ECTS = 30;
    private static final int MAX_SEMESTER = 12;

    private final String code;
    private String name;
    private int ects;
    private int semester;

    /**
     * Creates a course after validating the supplied data.
     *
     * @param code     the unique course code
     * @param name     the course name
     * @param ects     the ECTS credits (1 to {@value #MAX_ECTS})
     * @param semester the semester in which the course is taught
     *                 (1 to {@value #MAX_SEMESTER})
     * @throws ValidationException if the code or name is {@code null}, or the
     *                             ECTS or semester is out of range
     */
    public Course(String code, String name, int ects, int semester) {
        if (code == null) {
            throw new ValidationException("Course 'code' must not be null");
        }
        if (name == null) {
            throw new ValidationException("Course 'name' must not be null");
        }
        if (ects <= 0 || ects > MAX_ECTS) {
            throw new ValidationException("Course 'ects' out of range. [1, " + MAX_ECTS + "]");
        }
        if (semester <= 0 || semester > MAX_SEMESTER) {
            throw new ValidationException("Course 'semester' out of range. [1, " + MAX_SEMESTER + "]");
        }

        this.code = code;
        this.name = name;
        this.ects = ects;
        this.semester = semester;
    }

    /**
     * Returns the course's identity, which is its code.
     *
     * @return the course code
     */
    @Override
    public String getId() {
        return code;
    }

    /**
     * Compares this course with another object for equality by their code.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a course with the same code,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code derived from the code
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    /**
     * Returns the course name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the course name.
     *
     * @param name the new name
     * @throws ValidationException if {@code name} is {@code null}
     */
    public void setName(String name) {
        if (name == null) {
            throw new ValidationException("Course 'name' must not be null");
        }
        this.name = name;
    }

    /**
     * Returns the course's ECTS credits.
     *
     * @return the ECTS credits
     */
    public int getEcts() {
        return ects;
    }

    /**
     * Sets the course's ECTS credits.
     *
     * @param ects the new ECTS credits (1 to {@value #MAX_ECTS})
     * @throws ValidationException if the ECTS is out of range
     */
    public void setEcts(int ects) {
        if (ects <= 0 || ects > MAX_ECTS) {
            throw new ValidationException("Course 'ects' out of range. [1, " + MAX_ECTS + "]");
        }
        this.ects = ects;
    }

    /**
     * Returns the semester in which the course is taught.
     *
     * @return the semester
     */
    public int getSemester() {
        return semester;
    }

    /**
     * Sets the semester in which the course is taught.
     *
     * @param semester the new semester (1 to {@value #MAX_SEMESTER})
     * @throws ValidationException if the semester is out of range
     */
    public void setSemester(int semester) {
        if (semester <= 0 || semester > MAX_SEMESTER) {
            throw new ValidationException("Course 'semester' out of range. [1, " + MAX_SEMESTER + "]");
        }
        this.semester = semester;
    }
}
