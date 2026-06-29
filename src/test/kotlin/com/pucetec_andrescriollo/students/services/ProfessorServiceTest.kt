package com.pucetec_andrescriollo.students.services

import com.pucetec_andrescriollo.students.dto.ProfessorRequest
import com.pucetec_andrescriollo.students.entities.Professor
import com.pucetec_andrescriollo.students.exceptions.EmailAlreadyExistsException
import com.pucetec_andrescriollo.students.exceptions.ProfessorNotFound
import com.pucetec_andrescriollo.students.repositories.ProfessorRepository
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
class ProfessorServiceTest {

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var professorService: ProfessorService

    @Test
    fun `createProfessor returns response when email is not registered`() {
        val request = ProfessorRequest(name = "Dr. Smith", email = "smith@puce.edu")
        val savedProfessor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        whenever(professorRepository.existsByEmail(request.email)).thenReturn(false)
        whenever(professorRepository.save(any())).thenReturn(savedProfessor)

        val response = professorService.createProfessor(request)

        assertEquals(1L, response.id)
        assertEquals("Dr. Smith", response.name)
        assertEquals("smith@puce.edu", response.email)
    }

    @Test
    fun `createProfessor throws EmailAlreadyExistsException when email is already registered`() {
        val request = ProfessorRequest(name = "Dr. Smith", email = "smith@puce.edu")
        whenever(professorRepository.existsByEmail(request.email)).thenReturn(true)

        assertFailsWith<EmailAlreadyExistsException> {
            professorService.createProfessor(request)
        }
    }

    @Test
    fun `getAllProfessors returns list of professors`() {
        val professor1 = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val professor2 = Professor(id = 2L, name = "Dr. Johnson", email = "johnson@puce.edu")
        whenever(professorRepository.findAll()).thenReturn(listOf(professor1, professor2))

        val responses = professorService.getAllProfessors()

        assertEquals(2, responses.size)
        assertEquals(1L, responses[0].id)
        assertEquals("Dr. Smith", responses[0].name)
        assertEquals(2L, responses[1].id)
        assertEquals("Dr. Johnson", responses[1].name)
    }

    @Test
    fun `getProfessorById returns professor when exists`() {
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        whenever(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        val response = professorService.getProfessorById(1L)

        assertEquals(1L, response.id)
        assertEquals("Dr. Smith", response.name)
    }

    @Test
    fun `getProfessorById throws ProfessorNotFound when not exists`() {
        whenever(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFound> {
            professorService.getProfessorById(99L)
        }
    }

    @Test
    fun `updateProfessor returns response when email is not changed`() {
        val request = ProfessorRequest(name = "Dr. Smith Updated", email = "smith@puce.edu")
        val existingProfessor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val updatedProfessor = Professor(id = 1L, name = "Dr. Smith Updated", email = "smith@puce.edu")
        whenever(professorRepository.findById(1L)).thenReturn(Optional.of(existingProfessor))
        whenever(professorRepository.save(any())).thenReturn(updatedProfessor)

        val response = professorService.updateProfessor(1L, request)

        assertEquals(1L, response.id)
        assertEquals("Dr. Smith Updated", response.name)
        assertEquals("smith@puce.edu", response.email)
    }

    @Test
    fun `updateProfessor returns response when email is changed but new email is not registered`() {
        val request = ProfessorRequest(name = "Dr. Smith Updated", email = "smith.new@puce.edu")
        val existingProfessor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        val updatedProfessor = Professor(id = 1L, name = "Dr. Smith Updated", email = "smith.new@puce.edu")
        whenever(professorRepository.findById(1L)).thenReturn(Optional.of(existingProfessor))
        whenever(professorRepository.existsByEmail("smith.new@puce.edu")).thenReturn(false)
        whenever(professorRepository.save(any())).thenReturn(updatedProfessor)

        val response = professorService.updateProfessor(1L, request)

        assertEquals(1L, response.id)
        assertEquals("Dr. Smith Updated", response.name)
        assertEquals("smith.new@puce.edu", response.email)
    }

    @Test
    fun `updateProfessor throws EmailAlreadyExistsException when email is changed and new email is registered`() {
        val request = ProfessorRequest(name = "Dr. Smith Updated", email = "smith.new@puce.edu")
        val existingProfessor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        whenever(professorRepository.findById(1L)).thenReturn(Optional.of(existingProfessor))
        whenever(professorRepository.existsByEmail("smith.new@puce.edu")).thenReturn(true)

        assertFailsWith<EmailAlreadyExistsException> {
            professorService.updateProfessor(1L, request)
        }
    }

    @Test
    fun `updateProfessor throws ProfessorNotFound when professor not exists`() {
        val request = ProfessorRequest(name = "Dr. Smith Updated", email = "smith@puce.edu")
        whenever(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFound> {
            professorService.updateProfessor(99L, request)
        }
    }

    @Test
    fun `deleteProfessor deletes professor when exists`() {
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@puce.edu")
        whenever(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        professorService.deleteProfessor(1L)
    }

    @Test
    fun `deleteProfessor throws ProfessorNotFound when not exists`() {
        whenever(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertFailsWith<ProfessorNotFound> {
            professorService.deleteProfessor(99L)
        }
    }
}
