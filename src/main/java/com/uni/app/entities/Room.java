package com.uni.app.entities;

import com.uni.app.enums.RoomType;
import com.uni.app.exceptions.ValidationException;

import java.util.Objects;

/**
 * Represents a room, identified by its id.
 *
 * <p>Holds the room type, capacity and the {@link Building} it belongs to. A
 * room is used both as the location of a {@link Session} and as a professor's
 * office. Equality and hashing are based on the id.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Identifiable
 * @since 1.0
 */
public final class Room implements Identifiable {
    private static final int MAX_CAPACITY = 500;

    private final String id;
    private RoomType type;
    private int capacity;
    private final Building building;

    /**
     * Creates a room after validating the supplied data.
     *
     * @param id       the unique room id
     * @param type     the room type
     * @param capacity the number of people the room holds (1 to {@value #MAX_CAPACITY})
     * @param building the building the room belongs to
     * @throws ValidationException if the id, type or building is {@code null}, or
     *                             the capacity is out of range
     */
    public Room(String id, RoomType type, int capacity, Building building) {
        if (id == null) {
            throw new ValidationException("Room 'id' must not be null");
        }
        if (type == null) {
            throw new ValidationException("Room 'type' must not be null");
        }
        if (capacity <= 0 || capacity > MAX_CAPACITY) {
            throw new ValidationException("Room 'capacity' out of range. [1, " + MAX_CAPACITY + "]");
        }
        if (building == null) {
            throw new ValidationException("Room 'building' must not be null");
        }

        this.id = id;
        this.type = type;
        this.capacity = capacity;
        this.building = building;
    }

    /**
     * Returns the room's identity.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Compares this room with another object for equality by their id.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a room with the same id,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id);
    }

    /**
     * Computes a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code derived from the id
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Returns a short textual representation containing the id.
     *
     * @return a string with the room id
     */
    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                '}';
    }

    /**
     * Returns the room type.
     *
     * @return the type
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Sets the room type.
     *
     * @param type the new room type
     * @throws ValidationException if {@code type} is {@code null}
     */
    public void setType(RoomType type) {
        if (type == null) {
            throw new ValidationException("Room 'type' must not be null");
        }
        this.type = type;
    }

    /**
     * Returns the room capacity.
     *
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the room capacity.
     *
     * @param capacity the new capacity (1 to {@value #MAX_CAPACITY})
     * @throws ValidationException if the capacity is out of range
     */
    public void setCapacity(int capacity) {
        if (capacity <= 0 || capacity > MAX_CAPACITY) {
            throw new ValidationException("Room 'capacity' out of range. [1, " + MAX_CAPACITY + "]");
        }
        this.capacity = capacity;
    }

    /**
     * Returns the building the room belongs to.
     *
     * @return the building
     */
    public Building getBuilding() {
        return building;
    }
}
