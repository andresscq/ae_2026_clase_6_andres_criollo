package com.pucetec_andrescriollo.students.services

import com.pucetec_andrescriollo.students.dto.SubjectRequest
import com.pucetec_andrescriollo.students.entities.Professor
import com.pucetec_andrescriollo.students.entities.Subject
import com.pucetec_andrescriollo.students.exceptions.ProfessorNotFound
import com.pucetec_andrescriollo.students.exceptions.SubjectNotFoundException
import com.pucetec_andrescriollo.students.repositories.ProfessorRepository
import com.pucetec_andrescriollo.students.repositories.SubjectRepository
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


@ExtendWith(value = [MockitoExtension::class])
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    @Test
    fun `createSubject returns response when professor exists`() {
        val request = SubjectRequest(name = "Mathematics", code = "MAT101", professorId = 1L)
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val savedSubject = Subject(
            id = 1L,
            name = "Mathematics",
            code = "MAT101",
            professor = professor
        )
        whenever(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        whenever(subjectRepository.save(any())).thenReturn(savedSubject)

        val response = subjectService.createSubject(request)

        assertEquals(1L, response.id)
        assertEquals("Mathematics", response.name)
        assertEquals("MAT101", response.code)
        assertEquals(1L, response.professor.id)
    }

    @Test
    fun `createSubject throws ProfessorNotFound when professor does not exist`() {
        val request = SubjectRequest(name = "Mathematics", code = "MAT101", professorId = 99L)
        whenever(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFound> {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `getAllSubjects returns list of subjects`() {
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val subject1 = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        val subject2 = Subject(id = 2L, name = "Physics", code = "PHY101", professor = professor)
        whenever(subjectRepository.findAll()).thenReturn(listOf(subject1, subject2))

        val responses = subjectService.getAllSubjects()

        assertEquals(2, responses.size)
        assertEquals(1L, responses[0].id)
        assertEquals("Mathematics", responses[0].name)
        assertEquals(2L, responses[1].id)
        assertEquals("Physics", responses[1].name)
    }

    @Test
    fun `getSubjectById returns subject when exists`() {
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val subject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        whenever(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        val response = subjectService.getSubjectById(1L)

        assertEquals(1L, response.id)
        assertEquals("Mathematics", response.name)
    }

    @Test
    fun `getSubjectById throws SubjectNotFoundException when not exists`() {
        whenever(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<SubjectNotFoundException> {
            subjectService.getSubjectById(99L)
        }
    }

    @Test
    fun `updateSubject returns response when subject and professor exist`() {
        val request = SubjectRequest(name = "Advanced Math", code = "MAT201", professorId = 1L)
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val existingSubject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        val updatedSubject = Subject(id = 1L, name = "Advanced Math", code = "MAT201", professor = professor)
        whenever(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        whenever(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        whenever(subjectRepository.save(any())).thenReturn(updatedSubject)

        val response = subjectService.updateSubject(1L, request)

        assertEquals(1L, response.id)
        assertEquals("Advanced Math", response.name)
        assertEquals("MAT201", response.code)
    }

    @Test
    fun `updateSubject throws SubjectNotFoundException when subject does not exist`() {
        val request = SubjectRequest(name = "Advanced Math", code = "MAT201", professorId = 1L)
        whenever(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<SubjectNotFoundException> {
            subjectService.updateSubject(99L, request)
        }
    }

    @Test
    fun `updateSubject throws ProfessorNotFound when professor does not exist`() {
        val request = SubjectRequest(name = "Advanced Math", code = "MAT201", professorId = 99L)
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val existingSubject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        whenever(subjectRepository.findById(1L)).thenReturn(Optional.of(existingSubject))
        whenever(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFound> {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `deleteSubject deletes subject when exists`() {
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val subject = Subject(id = 1L, name = "Mathematics", code = "MAT101", professor = professor)
        whenever(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        subjectService.deleteSubject(1L)
    }

    @Test
    fun `deleteSubject throws SubjectNotFoundException when not exists`() {
        whenever(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<SubjectNotFoundException> {
            subjectService.deleteSubject(99L)
        }
    }
}
