package com.pucetec_andrescriollo.students.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SubjectRequest(
    @field:NotBlank(message = "El nombre es obligatorio.")
    val name: String,

    @field:NotBlank(message = "El código es obligatorio.")
    val code: String,

    @field:NotNull(message = "El professorId es obligatorio.")
    val professorId: Long,
)

data class SubjectResponse(
    val id: Long,
    val name: String,
    val code: String,
    val professor: ProfessorResponse,
)
