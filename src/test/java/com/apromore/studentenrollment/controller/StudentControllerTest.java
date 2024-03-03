package com.apromore.studentenrollment.controller;

import com.apromore.studentenrollment.dto.EnrollmentDTO;
import com.apromore.studentenrollment.dto.StudentRequestDTO;
import com.apromore.studentenrollment.dto.StudentResponseDTO;
import com.apromore.studentenrollment.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentResponseDTO studentResponseDTO;
    private StudentRequestDTO studentRequestDTO;
    private EnrollmentDTO enrollmentDTO;

    @BeforeEach
    void setUp() {
        studentRequestDTO = new StudentRequestDTO("John", 18);
        studentResponseDTO = new StudentResponseDTO(1, "John", 18);
        enrollmentDTO = new EnrollmentDTO(2, 1);
    }

    @Test
    void shouldCreateStudent() throws Exception {
        given(studentService.createStudent(studentRequestDTO)).willReturn(studentResponseDTO);
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int)studentResponseDTO.getId())));
    }

    @Test
    void shouldRetrieveAllStudents() throws Exception {
        List<StudentResponseDTO> allStudents = Collections.singletonList(studentResponseDTO);
        given(studentService.getAllStudents(null)).willReturn(allStudents);
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int)studentResponseDTO.getId())));
    }

    @Test
    void shouldRetrieveStudentById() throws Exception {
        given(studentService.retrieveStudentById(studentResponseDTO.getId())).willReturn(studentResponseDTO);

        mockMvc.perform(get("/api/v1/students/{studentId}", studentResponseDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int)studentResponseDTO.getId())));
    }

    @Test
    void shouldUpdateStudentById() throws Exception {
        mockMvc.perform(put("/api/v1/students/{studentId}", studentResponseDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteStudentByIdTest() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{studentId}", studentResponseDTO.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void enrollStudentToCourseTest() throws Exception {
        given(studentService.enrollStudentToCourse(studentResponseDTO.getId(), 2)).willReturn(enrollmentDTO);

        mockMvc.perform(post("/api/v1/students/{studentId}/courses/{courseId}", studentResponseDTO.getId(), 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentId", is((int) studentResponseDTO.getId())))
                .andExpect(jsonPath("$.courseId", is(2)));
    }

    @Test
    void unEnrollStudentsEnrolledToCourseTest() throws Exception {
        mockMvc.perform(delete("/api/v1/students/{studentId}/courses/{courseId}", studentResponseDTO.getId(), 1L))
                .andExpect(status().isOk());
    }

}