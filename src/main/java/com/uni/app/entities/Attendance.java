package com.uni.app.entities;

import com.uni.app.enums.AttendanceStatus;
import com.uni.app.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a student's attendance at a single {@link Session}, identified by
 * its id.
 *
 * <p>Pairs a student with a session through a {@link Relation} and records the
 * attendance {@link AttendanceStatus} together with the moment it was recorded.
 * Equality and hashing are based on the id.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @see Identifiable
 * @since 1.0
 */
public final class Attendance implements Identifiable {
    private final String id;
    private final Relation<Student, Session> relation;
    private AttendanceStatus status;
    private LocalDateTime recordedAt;

    /**
     * Creates an attendance record.
     *
     * @param id         the unique attendance id
     * @param relation   the student-session pair this record refers to
     * @param status     the attendance status
     * @param recordedAt the moment the status was recorded; if {@code null}, the
     *                   current time is used
     * @throws ValidationException if the id, relation or status is {@code null}
     */
    public Attendance(String id, Relation<Student, Session> relation, AttendanceStatus status, LocalDateTime recordedAt) {
        if (id == null) {
            throw new ValidationException("Attendance 'id' must not be null");
        }
        if (relation == null) {
            throw new ValidationException("Attendance 'relation' must not be null");
        }
        if (status == null) {
            throw new ValidationException("Attendance 'status' must not be null");
        }
        if (recordedAt == null) {
            recordedAt = LocalDateTime.now();
        }

        this.id = id;
        this.relation = relation;
        this.status = status;
        this.recordedAt = recordedAt;
    }

    /**
     * Returns the attendance record's identity.
     *
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Compares this attendance record with another object for equality by their id.
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is an attendance record with the same id,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attendance that = (Attendance) o;
        return Objects.equals(getId(), that.getId());
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
     * Returns a short textual representation containing the id.
     *
     * @return a string with the attendance id
     */
    @Override
    public String toString() {
        return "Attendance{" +
                "id='" + id + '\'' +
                '}';
    }

    /**
     * Returns the student-session pair this record refers to.
     *
     * @return the relation
     */
    public Relation<Student, Session> getRelation() {
        return relation;
    }

    /**
     * Returns the attendance status.
     *
     * @return the status
     */
    public AttendanceStatus getStatus() {
        return status;
    }

    /**
     * Sets the attendance status and updates the recorded time to the current moment.
     *
     * @param status the new attendance status
     * @throws ValidationException if {@code status} is {@code null}
     */
    public void setStatus(AttendanceStatus status) {
        if (status == null) {
            throw new ValidationException("Attendance 'status' must not be null");
        }
        this.status = status;
        this.recordedAt = LocalDateTime.now();
    }

    /**
     * Returns the moment the current status was recorded.
     *
     * @return the recorded date and time
     */
    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}
