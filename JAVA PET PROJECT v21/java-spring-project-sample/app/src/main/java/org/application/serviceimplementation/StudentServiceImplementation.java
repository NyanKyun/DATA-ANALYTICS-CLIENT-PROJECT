package org.application.serviceimplementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.application.model.Student;
import org.application.repository.StudentRepository;
import org.application.services.StudentService;

@Service
public class StudentServiceImplementation implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImplementation(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    //JPA Repository provides findAll(), findById(id), save(entity), delete(entity) 
    //All this functions will return a student object 
    @Override
    public List<Student> listAllStudents(){
        return studentRepository.findAll();        
    }

    //Test Curl for POST API
    //curl -X POST http://localhost:8080/api/student ^
    //-H "Content-Type: application/json" ^
    //-d "{""fullname"":""John Doe"",""courseId"":""CEEM900""}"
    @Override
    public String addNewStudent(Student student){
        String messageString = "SYSTEM API LOG: At addNewStudent(Student student) - Student object is empty";
        if (student != null) {
            studentRepository.save(student);
            messageString = "SYSTEM API LOG: successfully run addNewStudent(Student student)";
        }
        return messageString;
    }

    @Override
    public String deleteStudentService(UUID id){
        String messageString = "SYSTEM API LOG: At deleteStudent(Student student) - No student object provided";
        if (id == null){
            return messageString;
        }
        return studentRepository.findById(id).map(student -> {
            studentRepository.delete(student);
            return "SYSTEM API LOG: At deleteStudent(Student student) - Deleted Student";
        }).orElse("SYSTEM API LOG: Student not found");
    }

    //Optional<Student>  means value may or may not exist
    @Override
    public Optional<Student> getStudentById(UUID id){
        if (id == null){
            return Optional.empty();
        }
        Optional<Student> studentOpt = studentRepository.findById(id);
        studentOpt.ifPresent(s-> s.setLogMessage("SYSTEM API LOG: successfully run getStudentById(UUID id)"));
        return studentOpt;
    }
}