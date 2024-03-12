package com.codetest.studentenrollment.controller;

import com.codetest.studentenrollment.dto.EnrollmentDTO;
import com.codetest.studentenrollment.dto.StudentRequestDTO;
import com.codetest.studentenrollment.dto.StudentResponseDTO;
import com.codetest.studentenrollment.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO studentRequestDTO) {
        StudentResponseDTO studentResponse = studentService.createStudent(studentRequestDTO);
        return new ResponseEntity<>(studentResponse, new HttpHeaders(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> retrieveAllStudents(@RequestParam(required = false) Long courseId) {
        List<StudentResponseDTO> students = studentService.getAllStudents(courseId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> retrieveStudentById(@PathVariable long studentId) {
        StudentResponseDTO student = studentService.retrieveStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Void> updateStudentById(@PathVariable long studentId,
                                                  @RequestBody StudentRequestDTO studentRequestDTO) {
        studentService.updateStudentById(studentId, studentRequestDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable long studentId) {
        studentService.deleteStudentById(studentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<EnrollmentDTO> enrollStudentToCourse(@PathVariable long studentId,
                                                               @PathVariable long courseId) {
        EnrollmentDTO enrollment = studentService.enrollStudentToCourse(studentId, courseId);
        return new ResponseEntity<>(enrollment, new HttpHeaders(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{studentId}/courses/{courseId}")
    public ResponseEntity<Void> unEnrollStudentsEnrolledFromCourse(@PathVariable long studentId,
                                                                 @PathVariable long courseId) {
        studentService.unEnrollStudentsEnrolledToCourse(studentId, courseId);
        return ResponseEntity.ok().build();
    }
}
