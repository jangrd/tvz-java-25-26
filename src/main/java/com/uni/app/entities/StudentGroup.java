package com.uni.app.entities;

import com.uni.app.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * A named group of students managed by an administrator.
 *
 * <p>Professors use groups to bulk-enrol students into a {@link Session}.
 * A student may belong to more than one group.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class StudentGroup implements Identifiable {
    private final String id;
    private String name;
    private final List<Student> students;

    /**
     * Creates an empty group with no members.
     *
     * @param id   unique identifier of the group
     * @param name human-readable name of the group
     * @throws ValidationException if {@code id} or {@code name} is {@code null}
     */
    public StudentGroup(String id, String name) {
        this(id, name, new ArrayList<>());
    }

    /**
     * Creates a group pre-populated with the given student list.
     *
     * @param id       unique identifier of the group
     * @param name     human-readable name of the group
     * @param students initial members; a defensive copy is stored
     * @throws ValidationException if any argument is {@code null}
     */
    public StudentGroup(String id, String name, List<Student> students) {
        if (id == null) {
            throw new ValidationException("StudentGroup 'id' must not be null");
        }
        if (name == null) {
            throw new ValidationException("StudentGroup 'name' must not be null");
        }
        if (students == null) {
            throw new ValidationException("StudentGroup 'students' must not be null");
        }
        this.id = id;
        this.name = name;
        this.students = new ArrayList<>(students);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Returns the human-readable name of this group.
     *
     * @return the group name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of this group.
     *
     * @param name the new name
     * @throws ValidationException if {@code name} is {@code null}
     */
    public void setName(String name) {
        if (name == null) {
            throw new ValidationException("StudentGroup 'name' must not be null");
        }
        this.name = name;
    }

    /**
     * Returns a defensive copy of the student list.
     *
     * @return list of students currently in this group
     */
    public List<Student> getStudents() {
        return new ArrayList<>(this.students);
    }
}
