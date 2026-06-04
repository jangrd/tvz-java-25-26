package com.uni.app.entities;

import com.uni.app.exceptions.ValidationException;

import java.util.Objects;

/**
 * Represents a building, identified by its id.
 *
 * <p>Holds the building name and address; rooms reference the building they
 * belong to. Equality and hashing are based on the id.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Identifiable
 * @since 1.0
 */
public final class Building implements Identifiable {
    private final String id;
    private String name;
    private final String address;

    /**
     * Creates a building after validating the supplied data.
     *
     * @param id      the unique building id
     * @param name    the building name
     * @param address the building address
     * @throws ValidationException if the id, name or address is {@code null}
     */
    public Building(String id, String name, String address) {
        if (id == null) {
            throw new ValidationException("Building 'id' must not be null");
        }
        if (name == null) {
            throw new ValidationException("Building 'name' must not be null");
        }
        if (address == null) {
            throw new ValidationException("Building 'address' must not be null");
        }

        this.id = id;
        this.name = name;
        this.address = address;
    }

    /**
     * Returns the building's identity.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Compares this building with another object for equality by their id.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a building with the same id,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Building building = (Building) o;
        return Objects.equals(getId(), building.getId());
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code derived from the id
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    /**
     * Returns a short textual representation containing the id and name.
     *
     * @return a string with the building id and name
     */
    @Override
    public String toString() {
        return "Building{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Returns the building name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the building name.
     *
     * @param name the new name
     * @throws ValidationException if {@code name} is {@code null}
     */
    public void setName(String name) {
        if (name == null) {
            throw new ValidationException("Building 'name' must not be null");
        }
        this.name = name;
    }

    /**
     * Returns the building address.
     *
     * @return the immutable address
     */
    public String getAddress() {
        return address;
    }
}
