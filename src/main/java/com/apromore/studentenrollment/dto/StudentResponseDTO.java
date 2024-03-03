package com.apromore.studentenrollment.dto;

import com.apromore.studentenrollment.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {
    private long id;
    private String name;
    private int age;
    private List<Long> courseIds;
}
