package org.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.UUID;

import org.application.model.Student;
import org.application.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest //full app context
//@ActiveProfiles("test") //set active profile to test . can open this for type long. Spring will pick up setting file application-{profileName}.yml
@AutoConfigureMockMvc //real htpp call (no server)
@Transactional //DB auto rollback after each test
public class FullIntegrationStudentTest {

    //inject real beans (A Java object that Spring creates and manages for you)
    //Spring now:
    //  1. Creates the StudentService object
    //  2. Keeps it in memory
    //  3. Gives it to whoever needs it
    @Autowired
    private MockMvc mockMvc;

    //@Autowired
    //private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void shouldGetStudentById() throws Exception{
        Student student = new Student("Terry","CEEM600");
        Student savedStudent = studentRepository.save(student);
        UUID uuid = savedStudent.getStudentId();

        //actual + assertion
        //Here we actually test this on the repository so, must check value one by one
        mockMvc.perform(get("/api/student/{id}", uuid).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullname").value("Terry"))
            .andExpect(jsonPath("$.courseId").value("CEEM600"));
    }

}
