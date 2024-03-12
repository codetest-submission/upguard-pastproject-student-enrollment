package com.codetest.studentenrollment.service;

import com.codetest.studentenrollment.dto.EnrollmentDTO;
import com.codetest.studentenrollment.dto.StudentRequestDTO;
import com.codetest.studentenrollment.dto.StudentResponseDTO;
import com.codetest.studentenrollment.entity.Course;
import com.codetest.studentenrollment.entity.Student;
import com.codetest.studentenrollment.repository.CourseRepository;
import com.codetest.studentenrollment.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO) {
        Student studentToSave = this.entityFromStudentRequestDTO(studentRequestDTO);
        return this.entityToStudentResponseDTO(studentRepository.save(studentToSave));
    }

    public StudentResponseDTO retrieveStudentById(long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + studentId));
        return this.entityToStudentResponseDTO(student);
    }

    public void deleteStudentById(long studentId) {

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + studentId));
        student.getCourses().clear();
        studentRepository.save(student);
        studentRepository.deleteById(studentId);
    }

    public void updateStudentById(long studentId, StudentRequestDTO studentRequestDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + studentId));
        if (studentRequestDTO.getName() != null) {
            student.setName(studentRequestDTO.getName());
        }
        if (studentRequestDTO.getAge() != null) {
            student.setAge(studentRequestDTO.getAge());
        }
        studentRepository.save(student);
    }

    public List<StudentResponseDTO> getAllStudents(Long courseId) {
        if(courseId == null) {
            return studentRepository.findAll().stream().map(this::entityToStudentResponseDTO).collect(Collectors.toList());
        } else {
            Course course = courseRepository.findById(courseId).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with id: " + courseId));
            return course.getStudents().stream().map(this::entityToStudentResponseDTO).collect(Collectors.toList());
        }
    }

    public EnrollmentDTO enrollStudentToCourse(long studentId, long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        student.getCourses().add(course);
        studentRepository.save(student);
        return EnrollmentDTO.builder().studentId(studentId).courseId(courseId).build();
    }
    public void unEnrollStudentsEnrolledToCourse(long studentId, long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
        student.getCourses().remove(course);
        studentRepository.save(student);
    }

    private Student entityFromStudentRequestDTO(StudentRequestDTO studentRequestDTO) {
        return Student.builder().name(studentRequestDTO.getName()).age(studentRequestDTO.getAge()).build();
    }

    private StudentResponseDTO entityToStudentResponseDTO(Student student) {
        return StudentResponseDTO.builder().
                id(student.getId()).name(student.getName()).age(student.getAge()).
                courseIds(student.getCourses().stream().map(Course::getId).collect(Collectors.toList())).
                build();
    }
}

