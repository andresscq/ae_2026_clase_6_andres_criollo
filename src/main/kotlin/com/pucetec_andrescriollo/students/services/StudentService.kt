package com.pucetec_andrescriollo.students.services

import com.pucetec_andrescriollo.students.dto.StudentRequest
import com.pucetec_andrescriollo.students.dto.StudentResponse
import com.pucetec_andrescriollo.students.exceptions.EmailAlreadyExistsException
import com.pucetec_andrescriollo.students.exceptions.StudentNotFoundException
import com.pucetec_andrescriollo.students.mappers.toEntity
import com.pucetec_andrescriollo.students.mappers.toResponse
import com.pucetec_andrescriollo.students.repositories.StudentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {
    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    fun createStudent(request: StudentRequest): StudentResponse {
        logger.info("Creating Student ${request.name}")

        if (studentRepository.existsByEmail(request.email)) {
            logger.warn("Attempted to register duplicate email: ${request.email}")
            throw EmailAlreadyExistsException("El correo ${request.email} ya está registrado.")
        }

        val saved = studentRepository.save(request.toEntity())
        logger.info("Saved student with id ${saved.id}")
        return saved.toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        logger.info("Get all Students")
        return studentRepository.findAll().map { it.toResponse() }
    }

    fun getStudentById(id: Long): StudentResponse {
        logger.info("Get student id=$id")
        return findStudentOrThrow(id).toResponse()
    }

    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        logger.info("Updating student id=$id")
        val student = findStudentOrThrow(id)

        if (student.email != request.email && studentRepository.existsByEmail(request.email)) {
            logger.warn("Email conflict on update: ${request.email}")
            throw EmailAlreadyExistsException("El correo ${request.email} ya está registrado.")
        }

        student.name = request.name
        student.email = request.email
        return studentRepository.save(student).toResponse()
    }

    fun deleteStudent(id: Long) {
        logger.info("Deleting student id=$id")
        val student = findStudentOrThrow(id)
        studentRepository.delete(student)
    }

    private fun findStudentOrThrow(id: Long) =
        studentRepository.findById(id).orElseThrow {
            logger.warn("Student not found id=$id")
            StudentNotFoundException("Estudiante con id $id no encontrado.")
        }
}
