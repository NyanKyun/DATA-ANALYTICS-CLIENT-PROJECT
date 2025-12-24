package org.application.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "student_id", updatable = false, nullable = false)
    private UUID studentId;
    @Column(name = "fullname", nullable = false)
    private String fullname;
    @Column(name = "course_id", nullable = false)
    private String courseId;
    private String logMessage;
    
    protected Student(){}

    public Student(String fullname, String courseId){
        this.fullname = fullname;
        this.courseId = courseId;
    }

    @Override
    public String toString(){
        return String.format("Student ID: %s , Course ID: %s, Student Fullname: %s", 
        studentId, courseId, fullname);
    }

    public UUID getStudentId(){ return this.studentId; }

    public void setCourseId(String course_id){
        this.courseId = course_id; 
    }
    public String getCourseId(){ return this.courseId; }

    public void setLogMessage(String logMessage){
        this.logMessage = logMessage; 
    }
    public String getLogMessage(){ return this.logMessage; }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }
    public String getFullname(){ return this.fullname; } 
}
