package com.tecnocampus.tecnomeet.application;

import com.tecnocampus.tecnomeet.application.dto.StudentDTO;
import com.tecnocampus.tecnomeet.domain.Student;
import com.tecnocampus.tecnomeet.persistence.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentDTO register(StudentDTO dto) {
        Student s = new Student(dto.name(), dto.email(), dto.bio(), dto.photo());
        repository.addStudent(s);
        return toDTO(s);
    }

    public StudentDTO update(String id, StudentDTO dto) {
        Student s = repository.getStudentById(id);
        s.setName(dto.name());
        s.setEmail(dto.email());
        s.setBio(dto.bio());
        s.setPhoto(dto.photo());
        repository.updateStudent(s);
        return toDTO(s);
    }

    public StudentDTO getById(String id) {
        return toDTO(repository.getStudentById(id));
    }

    public List<StudentDTO> getAll() {
        return repository.getAllStudents().stream().map(this::toDTO).toList();
    }

    private StudentDTO toDTO(Student s) {
        return new StudentDTO(s.getId(), s.getName(), s.getEmail(), s.getBio(), s.getPhoto());
    }
}
