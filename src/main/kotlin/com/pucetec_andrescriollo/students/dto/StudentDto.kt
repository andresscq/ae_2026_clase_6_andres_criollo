package com.pucetec_andrescriollo.students.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class StudentRequest(
    @field:NotBlank(message = "El nombre es obligatorio.")
    val name: String,

    @field:NotBlank(message = "El email es obligatorio.")
    @field:Email(message = "El email no tiene un formato válido.")
    val email: String,
)

data class StudentResponse(
    val id: Long,
    val name: String,
    val email: String?,
)
