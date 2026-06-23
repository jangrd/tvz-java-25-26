package com.uni.app.database;

import com.uni.app.entities.Student;
import com.uni.app.entities.StudentGroup;
import com.uni.app.enums.StudyProgramme;
import com.uni.app.exceptions.DatabaseException;
import com.uni.app.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Provides persistence for {@link StudentGroup} entities backed by the
 * {@code student_group} table and membership management via
 * {@code student_group_membership}.
 *
 * <p>{@link #findAll()} and {@link #findById(String)} return groups without
 * their member lists; call {@link #findStudents(String)} to load the members
 * of a specific group.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class StudentGroupRepository extends AbstractRepository<StudentGroup> {

    private final String sqlInsert;
    private final String sqlUpdate;
    private final String sqlInsertMember;
    private final String sqlDeleteMember;
    private final String sqlFindStudents;

    /**
     * Creates a repository bound to the student_group table.
     *
     * @param db the database manager used to run statements
     * @throws ValidationException if {@code db} is {@code null}, or the table or
     *                             id column does not exist
     * @throws DatabaseException   if checking the schema fails
     */
    public StudentGroupRepository(DatabaseManager db) throws DatabaseException {
        super(db, StudentGroupRepository::mapRow, "student_group", "student_group_id");
        sqlInsert = "INSERT INTO %s (student_group_id, student_group_name) VALUES (?, ?);".formatted(getTable());
        sqlUpdate = "UPDATE %s SET student_group_name = ? WHERE %s = ?;".formatted(getTable(), getIdColumn());
        sqlInsertMember = """
                INSERT INTO student_group_membership
                (student_group_membership_student_jmbag, student_group_membership_student_group_id)
                VALUES (?, ?);""";
        sqlDeleteMember = """
                DELETE FROM student_group_membership
                WHERE student_group_membership_student_jmbag = ?
                AND student_group_membership_student_group_id = ?;""";
        sqlFindStudents = """
                SELECT st.student_jmbag, st.student_oib, st.student_first_name, st.student_last_name,
                       st.student_email, st.student_dob, st.student_year_of_study, st.student_study_programme
                FROM student st
                JOIN student_group_membership sgm
                     ON sgm.student_group_membership_student_jmbag = st.student_jmbag
                WHERE sgm.student_group_membership_student_group_id = ?;""";
    }

    /**
     * Inserts a new student group into the database.
     *
     * @param group the group to insert
     * @return the number of rows inserted
     * @throws ValidationException if {@code group} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int save(StudentGroup group) throws DatabaseException {
        if (group == null) {
            throw new ValidationException("StudentGroupRepository 'group' must not be null");
        }
        return execute(sqlInsert, group.getId(), group.getName());
    }

    /**
     * Updates the stored group selected by the given group's id.
     *
     * @param group the group carrying the new values; its id selects the row
     * @return the number of rows updated
     * @throws ValidationException if {@code group} is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int update(StudentGroup group) throws DatabaseException {
        if (group == null) {
            throw new ValidationException("StudentGroupRepository 'group' must not be null");
        }
        return execute(sqlUpdate, group.getName(), group.getId());
    }

    /**
     * Adds a student to a group.
     *
     * @param groupId      id of the target group
     * @param studentJmbag JMBAG of the student to add
     * @return the number of rows inserted
     * @throws ValidationException if either argument is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int addStudent(String groupId, String studentJmbag) throws DatabaseException {
        if (groupId == null) {
            throw new ValidationException("StudentGroupRepository 'groupId' must not be null");
        }
        if (studentJmbag == null) {
            throw new ValidationException("StudentGroupRepository 'studentJmbag' must not be null");
        }
        return execute(sqlInsertMember, studentJmbag, groupId);
    }

    /**
     * Removes a student from a group.
     *
     * @param groupId      id of the target group
     * @param studentJmbag JMBAG of the student to remove
     * @return the number of rows deleted
     * @throws ValidationException if either argument is {@code null}
     * @throws DatabaseException   if the statement fails
     */
    public int removeStudent(String groupId, String studentJmbag) throws DatabaseException {
        if (groupId == null) {
            throw new ValidationException("StudentGroupRepository 'groupId' must not be null");
        }
        if (studentJmbag == null) {
            throw new ValidationException("StudentGroupRepository 'studentJmbag' must not be null");
        }
        return execute(sqlDeleteMember, studentJmbag, groupId);
    }

    /**
     * Returns all students that belong to the given group.
     *
     * @param groupId id of the group whose members to fetch
     * @return list of students in the group, empty if the group has no members
     * @throws ValidationException if {@code groupId} is {@code null}
     * @throws DatabaseException   if the query fails
     */
    public List<Student> findStudents(String groupId) throws DatabaseException {
        if (groupId == null) {
            throw new ValidationException("StudentGroupRepository 'groupId' must not be null");
        }
        return queryWith(sqlFindStudents, StudentGroupRepository::mapStudent, groupId);
    }

    private static StudentGroup mapRow(ResultSet rs) throws SQLException {
        return new StudentGroup(
                rs.getString("student_group_id"),
                rs.getString("student_group_name")
        );
    }

    private static Student mapStudent(ResultSet rs) throws SQLException {
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
