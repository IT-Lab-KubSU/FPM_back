package ru.kubsu.fktpm.fpmbackend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.kubsu.fktpm.fpmbackend.service.curriculum.CurriculumService
import ru.kubsu.fktpm.fpmbackend.service.curriculum.dto.CurriculumRequest

@Tag(name = "Curriculum", description = "Curriculum endpoints")
@CrossOrigin
@RestController
@RequestMapping("/api/curriculum")
class CurriculumController(@Autowired val curriculumService: CurriculumService) {
    @PostMapping
    fun saveCurriculum(@RequestBody curriculumRequest: CurriculumRequest) =
        curriculumService.save(curriculumRequest)

    @GetMapping
    fun getCurriculum(
        @RequestParam directionCode: String,
        @RequestParam department: String
    ) =
        curriculumService.getById(directionCode, department)
}