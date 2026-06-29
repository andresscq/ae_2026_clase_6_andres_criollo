package com.pucetec_andrescriollo.students.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class EnrollmentRequest(
    @field:NotNull(message = "El studentId es obligatorio.")
    val studentId: Long,

    @field:NotNull(message = "El subjectId es obligatorio.")
    val subjectId: Long,

    @field:NotBlank(message = "El estado es obligatorio.")
    val status: String = "INSCRITO",
)

data class EnrollmentUpdateRequest(
    @field:NotBlank(message = "El estado es obligatorio.")
    val status: String,
)

data class EnrollmentResponse(
    val id: Long,
    val createdAt: String,
    val status: String,
    val subject: SubjectResponse,
    val student: StudentResponse,
)
