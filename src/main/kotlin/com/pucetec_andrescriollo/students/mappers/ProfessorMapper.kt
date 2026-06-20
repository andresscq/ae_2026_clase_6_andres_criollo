package com.pucetec_andrescriollo.students.mappers

import com.pucetec_andrescriollo.students.dto.ProfessorRequest
import com.pucetec_andrescriollo.students.dto.ProfessorResponse
import com.pucetec_andrescriollo.students.entities.Professor

fun ProfessorRequest.toEntity() = Professor(
    name = this.name,
    email = this.email
)

fun Professor.toResponse() = ProfessorResponse(
    id = this.id,
    name = this.name,
    email = this.email
)
