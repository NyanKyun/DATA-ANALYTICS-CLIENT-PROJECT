package org.application;

//Unit Test Dependencies
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//MockMVC Test Dependencies
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//Built in JAVA class Dependencies
import java.util.List;
import java.util.Optional;
import java.util.UUID;
//Own Implementation Dependencies
import org.application.model.Student;
import org.application.services.StudentService;
import org.application.controller.ApiController;
//This is a slice test where no server is run
//Loads only the Spring MVC components needed for your controller (like @Controller, @RestController, @ControllerAdvice, etc.).
//Does not start the full Spring Boot application or a server.
//Focused on testing web layer behavior like endpoints, request mappings, and responses.
@WebMvcTest( //@WebMvcTest → lightweight, controller-focused testing.
    controllers = ApiController.class, //Only load ApiController for this test.
    excludeAutoConfiguration = SecurityAutoConfiguration.class //Skip Spring Security setup to avoid authentication issues during testing.
)
//Loads only the given configuration class (WebApp.class in this case) instead of the whole application context.
//Useful for slice testing or when you want a custom context setup.
@ContextConfiguration(classes = WebApp.class) //@ContextConfiguration → precise control of what Spring configuration your test uses.
class ApiControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    void testGetStudentListController() throws Exception{
        List<Student> studentMockList = List.of(new Student("Jonathan","CEEM900"));
        when(studentService.listAllStudents()).thenReturn(studentMockList);

        mockMvc.perform(get("/api/students"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].fullname").value("Jonathan"))
            .andExpect(jsonPath("$[0].courseId").value("CEEM900"));
    }

    @Test
    void testGetVersionString() throws Exception{
        mockMvc.perform(get("/api/"))
            .andExpect(status().isOk())
            .andExpect(content().string("Web Service Version 0.0.1"));
    }

    @Test
    void testAddStudentController() throws Exception{
        when(studentService.addNewStudent(Mockito.any(Student.class)))
            .thenReturn("SYSTEM API LOG: successfully run addNewStudent(Student student)");

        mockMvc.perform(post("/api/student")   // use POST
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"fullname\":\"Jonathan\",\"courseId\":\"CEEM900\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("{message:successfully added student}"));
    }

    @Test
    void testDeleteStudentController() throws Exception {
        UUID uuid = UUID.fromString("3c5b9235-4ceb-4c3d-907f-695779cc87e0");

        // Mock the service call
        when(studentService.deleteStudentService(Mockito.any(UUID.class)))
            .thenReturn("SYSTEM API LOG: At deleteStudent(Student student) - Deleted Student");

        // Perform DELETE request
        mockMvc.perform(delete("/api/student/{id}", uuid)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("SYSTEM API LOG: At deleteStudent(Student student) - Deleted Student"));

        // Verify service method was called
        Mockito.verify(studentService, Mockito.times(1)).deleteStudentService(uuid);
    }

    @Test
    void testGetStudentById() throws Exception{
        UUID uuid = UUID.fromString("3c5b9235-4ceb-4c3d-907f-695779cc87e0");
        Student student = new Student("Jonathan", "CEEM900");

        when(studentService.getStudentById(Mockito.any(UUID.class))).thenReturn(Optional.of(student));

        //Here , we don't touch the DB so we don't worry about the ID differences 
        mockMvc.perform(get("/api/student/{id}", uuid)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(
                objectMapper.writeValueAsString(student)));

        // Verify service method was called
        Mockito.verify(studentService, Mockito.times(1)).getStudentById(uuid);
    }

}
