package com.uni.app.entities;

import com.uni.app.enums.SessionType;
import com.uni.app.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a single teaching session (one scheduled class meeting),
 * identified by its id.
 *
 * <p>A session ties a {@link Course} to the {@link Professor} who holds it, at a
 * given date and time, in a given room and of a given {@link SessionType}.
 * Attendance is recorded per session.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Identifiable
 * @since 1.0
 */
public final class Session implements Identifiable, Schedulable {
    private final String id;
    private SessionType type;
    private Course course;
    private Professor professor;
    private LocalDateTime dateTime;
    private int roomNumber;

    /**
     * Creates a session.
     *
     * @param id         the unique session id
     * @param type       the session type
     * @param course     the course being taught
     * @param professor  the professor holding the session
     * @param dateTime   the date and time of the session
     * @param roomNumber the room number (must be positive)
     * @throws ValidationException if the id, type, course, professor or
     *                             dateTime is {@code null}, or the room number
     *                             is not positive
     */
    public Session(String id, SessionType type, Course course, Professor professor, LocalDateTime dateTime, int roomNumber) {
        if (id == null) {
            throw new ValidationException("Session 'id' must not be null");
        }
        if (type == null) {
            throw new ValidationException("Session 'type' must not be null");
        }
        if (course == null) {
            throw new ValidationException("Session 'course' must not be null");
        }
        if (professor == null) {
            throw new ValidationException("Session 'professor' must not be null");
        }
        if (dateTime == null) {
            throw new ValidationException("Session 'dateTime' must not be null");
        }
        if (roomNumber <= 0) {
            throw new ValidationException("Session 'roomNumber' must be a positive integer");
        }

        this.id = id;
        this.type = type;
        this.course = course;
        this.professor = professor;
        this.dateTime = dateTime;
        this.roomNumber = roomNumber;
    }

    /**
     * Returns the session's identity.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Compares this session with another object for equality by their id.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a session with the same id,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(getId(), session.getId());
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
     * Returns the session type.
     *
     * @return the session type
     */
    public SessionType getType() {
        return type;
    }

    /**
     * Sets the session type.
     *
     * @param type the new session type
     * @throws ValidationException if {@code type} is {@code null}
     */
    public void setType(SessionType type) {
        if (type == null) {
            throw new ValidationException("Session 'type' must not be null");
        }
        this.type = type;
    }

    /**
     * Returns the course taught in this session.
     *
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course taught in this session.
     *
     * @param course the new course
     * @throws ValidationException if {@code course} is {@code null}
     */
    public void setCourse(Course course) {
        if (course == null) {
            throw new ValidationException("Session 'course' must not be null");
        }
        this.course = course;
    }

    /**
     * Returns the professor holding this session.
     *
     * @return the professor
     */
    public Professor getProfessor() {
        return professor;
    }

    /**
     * Sets the professor holding this session.
     *
     * @param professor the new professor
     * @throws ValidationException if {@code professor} is {@code null}
     */
    public void setProfessor(Professor professor) {
        if (professor == null) {
            throw new ValidationException("Session 'professor' must not be null");
        }
        this.professor = professor;
    }

    /**
     * Returns the date and time of this session.
     *
     * @return the date and time
     */
    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date and time of this session.
     *
     * @param dateTime the new date and time
     * @throws ValidationException if {@code dateTime} is {@code null}
     */
    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new ValidationException("Session 'dateTime' must not be null");
        }
        this.dateTime = dateTime;
    }

    /**
     * Returns the room number of this session.
     *
     * @return the room number
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * Sets the room number of this session.
     *
     * @param roomNumber the new room number (must be positive)
     * @throws ValidationException if the room number is not positive
     */
    public void setRoomNumber(int roomNumber) {
        if (roomNumber <= 0) {
            throw new ValidationException("Session 'roomNumber' must be a positive integer");
        }
        this.roomNumber = roomNumber;
    }
}
