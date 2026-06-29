package com.pucetec_andrescriollo.students.mappers

import com.pucetec_andrescriollo.students.dto.StudentRequest
import com.pucetec_andrescriollo.students.dto.StudentResponse
import com.pucetec_andrescriollo.students.entities.Student

fun StudentRequest.toEntity() = Student(
    name = this.name,
    email = this.email
)

fun Student.toResponse() = StudentResponse(
    id = this.id,
    name = this.name,
    email = this.email
)
