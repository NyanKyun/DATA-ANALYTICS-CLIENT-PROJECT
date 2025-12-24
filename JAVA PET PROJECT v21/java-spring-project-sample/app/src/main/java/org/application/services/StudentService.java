package org.application.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.application.model.Student;

public interface StudentService {
    List<Student> listAllStudents();
    String addNewStudent(Student student);
    String deleteStudentService(UUID id);
    Optional<Student> getStudentById(UUID id);
}
