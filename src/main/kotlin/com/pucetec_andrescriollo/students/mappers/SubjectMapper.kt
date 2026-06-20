package com.pucetec_andrescriollo.students.mappers

import com.pucetec_andrescriollo.students.dto.SubjectRequest
import com.pucetec_andrescriollo.students.dto.SubjectResponse
import com.pucetec_andrescriollo.students.entities.Professor
import com.pucetec_andrescriollo.students.entities.Subject

fun SubjectRequest.toEntity(professor: Professor) = Subject(
    name = this.name,
    code = this.code,
    professor = professor
)

fun Subject.toResponse() = SubjectResponse(
    id = this.id,
    name = this.name,
    code = this.code,
    professor = this.professor.toResponse()
)
