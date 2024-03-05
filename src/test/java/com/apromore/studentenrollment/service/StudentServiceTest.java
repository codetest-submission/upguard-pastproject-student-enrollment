package com.apromore.studentenrollment.service;

import com.apromore.studentenrollment.dto.EnrollmentDTO;
import com.apromore.studentenrollment.dto.StudentRequestDTO;
import com.apromore.studentenrollment.dto.StudentResponseDTO;
import com.apromore.studentenrollment.entity.Course;
import com.apromore.studentenrollment.entity.Student;
import com.apromore.studentenrollment.repository.CourseRepository;
import com.apromore.studentenrollment.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;
    @Mock
    private StudentRepository studentRepositoryMock;
    @Mock
    private CourseRepository courseRepositoryMock;

    @Test
    public void shouldCreateStudent() {
        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setName("John");
        requestDTO.setAge(20);

        Student student = new Student();
        student.setName("John");
        student.setAge(20);

        when(studentRepositoryMock.save(any())).thenReturn(student);

        StudentResponseDTO result = studentService.createStudent(requestDTO);

        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    public void shouldRetrieveStudentById() {
        long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        student.setName("John");

        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(student));

        StudentResponseDTO result = studentService.retrieveStudentById(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("John", result.getName());
    }

    @Test
    public void shouldRetrieveStudentByIdNotFound() {
        long studentId = 1L;

        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            studentService.retrieveStudentById(studentId);
        });
    }

    @Test
    public void shouldDeleteStudentById() {
        long studentId = 1L;
        Student student = new Student();
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(student));

        assertDoesNotThrow(() -> studentService.deleteStudentById(studentId));
    }

    @Test
    public void shouldThrowExceptionWhenDeleteStudentByIdWithIdNotFound() {
        long studentId = 1L;
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            studentService.deleteStudentById(studentId);
        });
    }

    @Test
    public void shouldUpdateStudentById() {
        long studentId = 1L;
        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setName("Updated Name");
        requestDTO.setAge(30);

        Student student = new Student();
        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(student));

        assertDoesNotThrow(() -> studentService.updateStudentById(studentId, requestDTO));

        assertEquals("Updated Name", student.getName());
        assertEquals(30, student.getAge());
    }

    @Test
    public void shouldGetAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        students.add(new Student());

        when(studentRepositoryMock.findAll()).thenReturn(students);

        List<StudentResponseDTO> result = studentService.getAllStudents(null);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void shouldGetEnrolledStudentsOfCourseById() {
        long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        Set<Student> students = new HashSet<>();
        students.add(new Student());
        students.add(new Student());

        course.setStudents(students);

        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(course));

        List<StudentResponseDTO> result = studentService.getAllStudents(courseId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void shouldGetEnrolledStudentsOfCourseByIdNotFound() {
        long courseId = 1L;

        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            studentService.getAllStudents(courseId);
        });
    }

    @Test
    public void shouldEnrollStudentToCourse() {
        long studentId = 1L;
        long courseId = 1L;
        Student student = new Student();
        student.setId(studentId);
        Course course = new Course();
        course.setId(courseId);

        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(course));

        EnrollmentDTO result = studentService.enrollStudentToCourse(studentId, courseId);

        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());
        assertEquals(courseId, result.getCourseId());
    }

    @Test
    public void shouldUnEnrollStudentsEnrolledToCourse() {
        long studentId = 1L;
        long courseId = 1L;
        Student student = new Student();
        student.setId(studentId);
        Course course = new Course();
        course.setId(courseId);
        course.getStudents().add(student);

        when(studentRepositoryMock.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepositoryMock.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepositoryMock.save(course)).thenReturn(course);

        assertDoesNotThrow(() -> studentService.unEnrollStudentsEnrolledToCourse(studentId, courseId));

        assertFalse(course.getStudents().contains(student));
    }
}
