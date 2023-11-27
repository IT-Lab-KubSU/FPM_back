package ru.kubsu.fktpm.fpmbackend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.kubsu.fktpm.fpmbackend.service.professor.ProfessorService
import ru.kubsu.fktpm.fpmbackend.service.professor.dto.Professor
import ru.kubsu.fktpm.fpmbackend.service.professor.dto.ProfessorRequest

@Tag(name = "Professors", description = "Professors endpoints")
@CrossOrigin
@RestController
@RequestMapping("/api/professors")
class ProfessorController(@Autowired val professorService: ProfessorService) {
    @PostMapping
    fun createProfessor(@RequestBody professorRequest: ProfessorRequest) = professorService.create(professorRequest)

    @GetMapping("/{id}")
    fun getProfessorById(@PathVariable("id") id: Long) = professorService.getById(id)

    @PutMapping
    fun updateProfessor(@RequestBody professor: Professor) = professorService.update(professor)

    @DeleteMapping("/{id}")
    fun deleteProfessor(@PathVariable("id") id: Long) = professorService.deleteById(id)
}