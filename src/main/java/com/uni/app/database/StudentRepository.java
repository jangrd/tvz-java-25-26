package com.uni.app.database;

import com.uni.app.entities.Student;
import com.uni.app.enums.StudyProgramme;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Provides persistence for {@link Student} entities backed by the
 * {@code student} table.
 *
 * <p>Inherits the read and delete operations from {@link AbstractRepository}
 * and adds the entity-specific {@link #save(Student)} and
 * {@link #update(Student)} writes. The study programme is stored as the enum
 * constant name and the date of birth as a SQL {@code DATE}.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class StudentRepository extends AbstractRepository<Student> {
    private final String sqlInsert;
    private final String sqlUpdate;

    /**
     * Creates a repository bound to one table.
     *
     * @param db the database manager used to run statements
     * @throws ValidationException if {@code db} is {@code null}, or the table or
     *                             id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public StudentRepository(DatabaseManager db) throws DatabaseException {
        super(db, StudentRepository::mapRow, "student", "student_jmbag");
        sqlInsert = """
            INSERT INTO %s
            (student_jmbag, student_oib, student_first_name, student_last_name,
             student_email, student_dob, student_year_of_study, student_study_programme)
            VALUES
            (?, ?, ?, ?, ?, ?, ?, ?);
            """.formatted(getTable());
        sqlUpdate = """
            UPDATE %s SET
            student_oib = ?, student_first_name = ?, student_last_name = ?, student_email = ?,
            student_dob = ?, student_year_of_study = ?, student_study_programme = ?
            WHERE %s = ?;
            """.formatted(getTable(), getIdColumn());
    }

    /**
     * Inserts a new student into the database.
     *
     * @param student the student to insert
     * @return the number of rows inserted
     * @throws ValidationException if {@code student} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int save(Student student) throws DatabaseException {
        if (student == null) {
            throw new ValidationException("StudentRepository 'student' must not be null");
        }
        return execute(
                sqlInsert,
                student.getJmbag(),
                student.getOib(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getDob(),
                student.getYearOfStudy(),
                student.getStudyProgramme().name()
        );
    }

    /**
     * Updates the stored student selected by the given student's JMBAG.
     *
     * @param student the student carrying the new values; its JMBAG selects the row
     * @return the number of rows updated
     * @throws ValidationException if {@code student} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int update(Student student) throws DatabaseException {
        if (student == null) {
            throw new ValidationException("StudentRepository 'student' must not be null");
        }
        return execute(
                sqlUpdate,
                student.getOib(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getDob(),
                student.getYearOfStudy(),
                student.getStudyProgramme().name(),
                student.getJmbag()
        );
    }

    private static Student mapRow(ResultSet rs) throws SQLException {
        return new Student.Builder()
                .jmbag(rs.getString("student_jmbag"))
                .oib(rs.getString("student_oib"))
                .firstName(rs.getString("student_first_name"))
                .lastName(rs.getString("student_last_name"))
                .email(rs.getString("student_email"))
                .dob(rs.getObject("student_dob", LocalDate.class))
                .yearOfStudy(rs.getInt("student_year_of_study"))
                .studyProgramme(StudyProgramme.valueOf(rs.getString("student_study_programme")))
                .build();
    }
}
