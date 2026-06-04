package com.uni.app.entities;

import com.uni.app.enums.RoomType;
import com.uni.app.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a professor, identified by their OIB.
 *
 * <p>Extends {@link Person} with the academic title, office and department.
 * Equality, hashing and the textual representation are inherited from
 * {@link Person} and are based on the OIB.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Person
 * @since 1.0
 */
public final class Professor extends Person {
    private String title;
    private Room office;
    private String department;

    /**
     * Creates a professor after validating the supplied data.
     *
     * @param oib        the professor's OIB
     * @param firstName  the professor's first name
     * @param lastName   the professor's last name
     * @param email      the professor's email address
     * @param dob        the date of birth
     * @param title      the academic title
     * @param office     the professor's office (must be of type {@link RoomType#OFFICE})
     * @param department the department the professor belongs to
     * @throws ValidationException if the title, department or office is
     *                             {@code null}, or the office is not of type
     *                             {@link RoomType#OFFICE}
     */
    public Professor(String oib, String firstName, String lastName, String email, LocalDate dob, String title, Room office, String department) {
        super(oib, firstName, lastName, email, dob);
        if (title == null) {
            throw new ValidationException("Professor 'title' must not be null");
        }
        if (department == null) {
            throw new ValidationException("Professor 'department' must not be null");
        }
        if (office == null) {
            throw new ValidationException("Professor 'office' must not be null");
        }
        if (office.getType() != RoomType.OFFICE) {
            throw new ValidationException("Professor 'office' must have RoomType.OFFICE");
        }

        this.title = title;
        this.office = office;
        this.department = department;
    }

    /**
     * Returns the professor's identity, which is the OIB.
     *
     * @return the OIB
     */
    @Override
    public String getId() {
        return getOib();
    }

    /**
     * Compares this professor with another object for equality by their OIB.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a professor with the same OIB,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(getId(), ((Professor) o).getId());
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code derived from the OIB
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /**
     * Returns the professor's academic title.
     *
     * @return the academic title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the professor's academic title.
     *
     * @param title the new academic title
     * @throws ValidationException if {@code title} is {@code null}
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new ValidationException("Professor 'title' must not be null");
        }
        this.title = title;
    }

    /**
     * Returns the professor's office.
     *
     * @return the office
     */
    public Room getOffice() {
        return office;
    }

    /**
     * Sets the professor's office.
     *
     * @param office the new office (must be of type {@link RoomType#OFFICE})
     * @throws ValidationException if {@code office} is {@code null} or is not of
     *                             type {@link RoomType#OFFICE}
     */
    public void setOffice(Room office) {
        if (office == null) {
            throw new ValidationException("Professor 'office' must not be null");
        }
        if (office.getType() != RoomType.OFFICE) {
            throw new ValidationException("Professor 'office' must have RoomType.OFFICE");
        }
        this.office = office;
    }

    /**
     * Returns the professor's department.
     *
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the professor's department.
     *
     * @param department the new department
     * @throws ValidationException if {@code department} is {@code null}
     */
    public void setDepartment(String department) {
        if (department == null) {
            throw new ValidationException("Professor 'department' must not be null");
        }
        this.department = department;
    }
}
