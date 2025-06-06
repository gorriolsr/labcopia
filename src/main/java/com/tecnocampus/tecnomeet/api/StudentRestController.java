package com.tecnocampus.tecnomeet.api;

import com.tecnocampus.tecnomeet.application.StudentService;
import com.tecnocampus.tecnomeet.application.dto.StudentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentRestController {
    private final StudentService service;

    public StudentRestController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDTO register(@RequestBody StudentDTO dto) {
        return service.register(dto);
    }

    @PutMapping("/{id}")
    public StudentDTO update(@PathVariable String id, @RequestBody StudentDTO dto) {
        return service.update(id, dto);
    }

    @GetMapping("/{id}")
    public StudentDTO get(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    public List<StudentDTO> getAll() {
        return service.getAll();
    }
}
