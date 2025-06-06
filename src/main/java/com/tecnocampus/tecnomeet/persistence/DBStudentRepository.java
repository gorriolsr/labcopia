package com.tecnocampus.tecnomeet.persistence;

import com.tecnocampus.tecnomeet.domain.Student;
import com.tecnocampus.outlaws.utilities.NotFoundException;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
@Profile("db-h2")
public class DBStudentRepository implements StudentRepository {
    private final JdbcClient jdbcClient;

    public DBStudentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getString("id"));
        s.setDeleted(rs.getBoolean("deleted"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setBio(rs.getString("bio"));
        s.setPhoto(rs.getString("photo"));
        s.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return s;
    }

    @Override
    public void addStudent(Student student) {
        var params = Map.of(
                "id", student.getId(),
                "name", student.getName(),
                "email", student.getEmail(),
                "bio", student.getBio(),
                "photo", student.getPhoto(),
                "createdAt", Timestamp.valueOf(student.getCreatedAt()),
                "deleted", student.isDeleted()
        );
        jdbcClient.sql("INSERT INTO STUDENTS (id,name,email,bio,photo,created_at,deleted) VALUES (:id,:name,:email,:bio,:photo,:createdAt,:deleted)")
                .params(params).update();
    }

    @Override
    public Student getStudentById(String id) {
        return jdbcClient.sql("SELECT * FROM STUDENTS WHERE id = :id")
                .param("id", id)
                .query(this::mapStudent)
                .stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    @Override
    public List<Student> getAllStudents() {
        return jdbcClient.sql("SELECT * FROM STUDENTS WHERE deleted = FALSE")
                .query(this::mapStudent).list();
    }

    @Override
    public void updateStudent(Student student) {
        int rows = jdbcClient.sql("UPDATE STUDENTS SET name=:name,email=:email,bio=:bio,photo=:photo,deleted=:deleted WHERE id=:id")
                .param("name", student.getName())
                .param("email", student.getEmail())
                .param("bio", student.getBio())
                .param("photo", student.getPhoto())
                .param("deleted", student.isDeleted())
                .param("id", student.getId())
                .update();
        if (rows == 0) throw new NotFoundException("Student not found");
    }

    @Override
    public void removeAll() {
        jdbcClient.sql("DELETE FROM STUDENTS").update();
    }
}
