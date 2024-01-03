package com.springbatchexample.processor;

import com.springbatchexample.entity.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentItemProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student student) throws Exception {
        // You can perform any transformations or validations here
        // For now, the processor simply returns the student without any changes
        return student;
    }
}
