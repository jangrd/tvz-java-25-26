package com.uni.app.entities;

import com.uni.app.exceptions.ValidationException;

import java.time.LocalDate;

/**
 * Represents a professor, identified by their OIB.
 *
 * <p>Extends {@link Person} with the academic title, office number and
 * department. Equality, hashing and the textual representation are inherited
 * from {@link Person} and are based on the OIB.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Person
 * @since 1.0
 */
public final class Professor extends Person {
    private String title;
    private int officeNumber;
    private String department;

    /**
     * Creates a professor after validating the supplied data.
     *
     * @param oib          the professor's OIB
     * @param firstName    the professor's first name
     * @param lastName     the professor's last name
     * @param email        the professor's email address
     * @param dob          the date of birth
     * @param title        the academic title
     * @param officeNumber the office number (must be positive)
     * @param department   the department the professor belongs to
     * @throws ValidationException if the title or department is {@code null}, or
     *                             the office number is not positive
     */
    public Professor(String oib, String firstName, String lastName, String email, LocalDate dob, String title, int officeNumber, String department) {
        super(oib, firstName, lastName, email, dob);
        if (title == null || department == null) {
            throw new ValidationException("Title and department must be provided");
        }
        if (officeNumber <= 0) {
            throw new ValidationException("Invalid office number");
        }

        this.title = title;
        this.officeNumber = officeNumber;
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
            throw new ValidationException("Title must be provided");
        }
        this.title = title;
    }

    /**
     * Returns the professor's office number.
     *
     * @return the office number
     */
    public int getOfficeNumber() {
        return officeNumber;
    }

    /**
     * Sets the professor's office number.
     *
     * @param officeNumber the new office number (must be positive)
     * @throws ValidationException if the office number is not positive
     */
    public void setOfficeNumber(int officeNumber) {
        if (officeNumber <= 0) {
            throw new ValidationException("Invalid office number");
        }
        this.officeNumber = officeNumber;
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
            throw new ValidationException("Department must be provided");
        }
        this.department = department;
    }
}
