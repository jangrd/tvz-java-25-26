package com.uni.app.entities;

import com.uni.app.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a person in the attendance system and holds the data shared by all
 * people, such as OIB, name, email and date of birth.
 *
 * <p>This class is abstract and is extended by concrete types such as
 * {@link Student}. Equality is based on the identity returned by
 * {@link #getId()}, so two people of different concrete types are never equal.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Identifiable
 * @since 1.0
 */
public sealed abstract class Person implements Identifiable permits Student, Professor {
    private final String oib;
    private String firstName;
    private String lastName;
    private String email;
    private final LocalDate dob;

    /**
     * Creates a person after validating the supplied data.
     *
     * <p>The OIB is validated against the ISO 7064 (MOD 11,10) check-digit
     * algorithm before the person is constructed.</p>
     *
     * @param oib       the 11-digit Croatian personal identification number
     * @param firstName the person's first name
     * @param lastName  the person's last name
     * @param email     the person's email address
     * @param dob       the date of birth
     * @throws ValidationException if any argument is {@code null}, the OIB is not
     *                             exactly 11 digits, or it fails check-digit validation
     */
    protected Person(String oib, String firstName, String lastName, String email, LocalDate dob) {
        if (oib == null) {
            throw new ValidationException("Person 'oib' must not be null");
        }
        if (firstName == null) {
            throw new ValidationException("Person 'firstName' must not be null");
        }
        if (lastName == null) {
            throw new ValidationException("Person 'lastName' must not be null");
        }
        if (email == null) {
            throw new ValidationException("Person 'email' must not be null");
        }
        if (dob == null) {
            throw new ValidationException("Person 'dob' must not be null");
        }
        if (!isValidOib(oib)) {
            throw new ValidationException("Person 'oib' not valid");
        }

        this.oib = oib;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
    }

    /**
     * Compares this person with another object for equality by their identity.
     *
     * <p>Two people are equal only when they are of the exact same concrete type
     * and share the same value of {@link #getId()}.</p>
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is of the same type and has the same id,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Person other = (Person) o;
        return Objects.equals(this.getId(), other.getId());
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code derived from {@link #getId()}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }

    /**
     * Returns a short textual representation of this person.
     *
     * @return the identity followed by the full name
     */
    @Override
    public String toString() {
        return this.getId() + " " + this.getFullName();
    }

    /**
     * Returns the person's full name.
     *
     * @return the first and last name joined by a space
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Returns the person's OIB.
     *
     * @return the immutable OIB
     */
    public String getOib() {
        return oib;
    }

    /**
     * Returns the person's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the person's first name.
     *
     * @param firstName the new first name
     * @throws ValidationException if {@code firstName} is {@code null}
     */
    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new ValidationException("Person 'firstName' must not be null");
        }
        this.firstName = firstName;
    }

    /**
     * Returns the person's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the person's last name.
     *
     * @param lastName the new last name
     * @throws ValidationException if {@code lastName} is {@code null}
     */
    public void setLastName(String lastName) {
        if (lastName == null) {
            throw new ValidationException("Person 'lastName' must not be null");
        }
        this.lastName = lastName;
    }

    /**
     * Returns the person's email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the person's email address.
     *
     * @param email the new email address
     * @throws ValidationException if {@code email} is {@code null}
     */
    public void setEmail(String email) {
        if (email == null) {
            throw new ValidationException("Person 'email' must not be null");
        }
        this.email = email;
    }

    /**
     * Returns the person's date of birth.
     *
     * @return the immutable date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Validates provided OIB.
     *
     * <p>Returns {@code true} only when all of the following hold:</p>
     * <ul>
     *     <li>the value is not {@code null};</li>
     *     <li>it is exactly 11 characters long;</li>
     *     <li>every character is a digit;</li>
     *     <li>the eleventh digit matches the control digit computed from the
     *         first ten using the ISO 7064 (MOD 11,10) algorithm.</li>
     * </ul>
     *
     * @param oib the OIB to validate
     * @return {@code true} if {@code oib} is valid,
     *         {@code false} otherwise
     * @see <a href="https://www.iso.org/standard/31531.html">ISO 7064</a>
     */
    private static boolean isValidOib(String oib) {
        if (oib == null) {
            return false;
        }
        if (oib.length() != 11) {
            return false;
        }
        if (!oib.chars().allMatch(Character::isDigit)) {
            return false;
        }
        int control = 10;
        for (char c : oib.substring(0, 10).toCharArray()) {
            control = (control + c - '0') % 10;
            if (control == 0) {
                control = 10;
            }
            control = (control * 2) % 11;
        }
        return (11 - control) % 10 == oib.charAt(10) - '0';
    }
}
