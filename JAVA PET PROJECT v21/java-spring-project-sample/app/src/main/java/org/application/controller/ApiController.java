package org.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.application.services.StudentService;
import org.application.model.Student;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StudentService studentService;

    public ApiController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String getApplicationVersion() {
        return "Web Service Version 0.0.1";
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.listAllStudents();
    }

    //Test Curl for posting
    //curl -X POST http://localhost:8080/api/student ^
    //-H "Content-Type: application/json" ^
    //-d "{""fullname"":""John Doe"",""courseId"":""CEEM900""}"
    @PostMapping("/student")
    public String addStudent(@RequestBody Student student) {
        if (student.getFullname() == null || student.getCourseId() == null){
            return "{\"message\":\"Student's information is incomplete\"}";
        }
        studentService.addNewStudent(student);
        return "{\"message\":\"successfully added student\"}";
    }

    //curl -X DELETE http://localhost:8080/api/students/b7cea48c-6832-479a-a9ab-934968e547eb
    @DeleteMapping("/student/{id}")
    public String deleteStudent(@PathVariable UUID id) {
        if (id == null){
            return "{message:Unable to delete student}";
        }
        return studentService.deleteStudentService(id);
    }
    
    //ResponseEntity<Student> - Used at the controller (API boundary).
    //Controls : status code (200, 404, 400, 500…), headers, response body
    //curl http://localhost:8080/api/student/3c5b9235-4ceb-4c3d-907f-695779cc87e0 -H "Accept: application/json"
    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable UUID id){
        return studentService.getStudentById(id)
        .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}