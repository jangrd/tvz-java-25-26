package com.uni.app.database;

import com.uni.app.entities.Course;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides persistence for {@link Course} entities backed by the
 * {@code course} table.
 *
 * <p>Inherits the read and delete operations from {@link AbstractRepository}
 * and adds the entity-specific {@link #save(Course)} and
 * {@link #update(Course)} writes.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class CourseRepository extends AbstractRepository<Course> {
    private final String sqlInsert;
    private final String sqlUpdate;

    /**
     * Creates a repository bound to the course table.
     *
     * @param db the database manager used to run statements
     * @throws ValidationException if {@code db} is {@code null}, or the table or
     *                             id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public CourseRepository(DatabaseManager db) throws DatabaseException {
        super(db, CourseRepository::mapRow, "course", "course_code");
        sqlInsert = """
                INSERT INTO %s (course_code, course_name, course_ects, course_semester)
                VALUES (?, ?, ?, ?);
                """.formatted(getTable());
        sqlUpdate = """
                UPDATE %s SET course_name = ?, course_ects = ?, course_semester = ?
                WHERE %s = ?;
                """.formatted(getTable(), getIdColumn());
    }

    /**
     * Inserts a new course into the database.
     *
     * @param course the course to insert
     * @return the number of rows inserted
     * @throws ValidationException if {@code course} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int save(Course course) throws DatabaseException {
        if (course == null) {
            throw new ValidationException("CourseRepository 'course' must not be null");
        }
        return execute(
                sqlInsert,
                course.getId(),
                course.getName(),
                course.getEcts(),
                course.getSemester()
        );
    }

    /**
     * Updates the stored course selected by the given course's code.
     *
     * @param course the course carrying the new values; its code selects the row
     * @return the number of rows updated
     * @throws ValidationException if {@code course} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int update(Course course) throws DatabaseException {
        if (course == null) {
            throw new ValidationException("CourseRepository 'course' must not be null");
        }
        return execute(
                sqlUpdate,
                course.getName(),
                course.getEcts(),
                course.getSemester(),
                course.getId()
        );
    }

    private static Course mapRow(ResultSet rs) throws SQLException {
        return new Course(
                rs.getString("course_code"),
                rs.getString("course_name"),
                rs.getInt("course_ects"),
                rs.getInt("course_semester")
        );
    }
}
