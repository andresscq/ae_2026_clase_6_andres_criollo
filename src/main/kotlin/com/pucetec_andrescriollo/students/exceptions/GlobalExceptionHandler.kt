package com.pucetec_andrescriollo.students.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailAlreadyExistsException(e: EmailAlreadyExistsException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            message = "Email already exists: ${e.message}",
        )
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(errorResponse)
    }

    @ExceptionHandler(DuplicateEnrollmentException::class)
    fun handleDuplicateEnrollmentException(e: DuplicateEnrollmentException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            message = e.message,
        )
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(errorResponse)
    }

    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFoundException(e: StudentNotFoundException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Estudiante no encontrado - ERROR",
            source = "StudentService"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }

    @ExceptionHandler(SubjectNotFoundException::class)
    fun handleSubjectNotFoundException(
        e: SubjectNotFoundException
    ): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Materia no encontrada - ERROR",
            source = "SubjectService"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }

    @ExceptionHandler(ProfessorNotFound::class)
    fun handleProfessorNotFound(
        e: ProfessorNotFound
    ): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Profesor no encontrado - ERROR",
            source = "ProfessorService"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }

    @ExceptionHandler(EnrollmentNotFoundException::class)
    fun handleEnrollmentNotFoundException(
        e: EnrollmentNotFoundException
    ): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(
            message = e.message ?: "Matrícula no encontrada - ERROR",
            source = "EnrollmentService"
        )
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        e: MethodArgumentNotValidException
    ): ResponseEntity<ValidationErrorResponse> {
        val errors = e.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Valor inválido")
        }
        val response = ValidationErrorResponse(
            message = "Errores de validación.",
            errors = errors,
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }
}

data class ErrorResponse(
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

data class ExceptionResponse(
    val message: String,
    val source: String
)

data class ValidationErrorResponse(
    val message: String,
    val errors: Map<String, String>,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
