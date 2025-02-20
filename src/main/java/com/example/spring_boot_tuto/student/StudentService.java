package com.example.spring_boot_tuto.student;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class StudentService {
  private final StudentRepository studentRepository;
          public StudentService( StudentRepository studentRepository) {
           this.studentRepository = studentRepository ;
          }
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {

      Optional<Student> studentByEmail =
              studentRepository.findStudentByEmail(student.getEmail());
      if (studentByEmail.isPresent()) {
          throw new IllegalStateException("email already taken");
      }
      studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists =  studentRepository.existsById(studentId);
         if (!exists) {
              throw new IllegalStateException("student with id " + studentId + " does not exist");
          }
        studentRepository.deleteById(studentId);
    }
    @Transactional
    public void updateStudent (Long studentId , String name , String email ) {
        Student student = studentRepository.findStudentById(studentId).orElseThrow(
                () -> new IllegalStateException("student id with " + studentId + "does not exist")
        );
        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name) ) {
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email) ) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
        }
}
