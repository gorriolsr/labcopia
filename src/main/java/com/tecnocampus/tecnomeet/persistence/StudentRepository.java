package com.tecnocampus.tecnomeet.persistence;

import com.tecnocampus.tecnomeet.domain.Student;

import java.util.List;

public interface StudentRepository {
    void addStudent(Student student);
    Student getStudentById(String id);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void removeAll();
}
