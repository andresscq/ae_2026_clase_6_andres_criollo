package com.pucetec_andrescriollo.students.controllers

import com.pucetec_andrescriollo.students.dto.StudentRequest
import com.pucetec_andrescriollo.students.dto.StudentResponse
import com.pucetec_andrescriollo.students.services.StudentService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/students")
class StudentController(
    private val studentService: StudentService,
) {
    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping
    fun createStudent(
        @Valid @RequestBody request: StudentRequest
    ): ResponseEntity<StudentResponse> {
        logger.info("Creating Student ${request.name}")
        val created = studentService.createStudent(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping
    fun getAllStudents(): ResponseEntity<List<StudentResponse>> {
        logger.info("Getting all students")
        return ResponseEntity.ok(studentService.getAllStudents())
    }

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: Long): ResponseEntity<StudentResponse> {
        logger.info("Getting student id=$id")
        return ResponseEntity.ok(studentService.getStudentById(id))
    }

    @PutMapping("/{id}")
    fun updateStudent(
        @PathVariable id: Long,
        @Valid @RequestBody request: StudentRequest
    ): ResponseEntity<StudentResponse> {
        logger.info("Updating student id=$id")
        return ResponseEntity.ok(studentService.updateStudent(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable id: Long): ResponseEntity<Void> {
        logger.info("Deleting student id=$id")
        studentService.deleteStudent(id)
        return ResponseEntity.noContent().build()
    }
}
