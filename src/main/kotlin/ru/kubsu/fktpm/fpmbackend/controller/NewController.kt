package ru.kubsu.fktpm.fpmbackend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.kubsu.fktpm.fpmbackend.service.New
import ru.kubsu.fktpm.fpmbackend.service.NewRequest
import ru.kubsu.fktpm.fpmbackend.service.NewService

@Tag(name = "News", description = "News endpoints")
@CrossOrigin
@RestController
@RequestMapping("/news")
class NewController(@Autowired val newService: NewService) {
    @PostMapping
    fun createNew(@RequestBody new: NewRequest) = newService.createNew(new)

    @GetMapping("/{id}")
    fun getNewById(@PathVariable("id") id: Long) = newService.getNewById(id)

    @GetMapping
    fun getNews(
        @RequestParam(defaultValue = "50") limit: Int,
        @RequestParam(defaultValue = "0") page: Int
    ) = newService.getNews(limit, page)

    @PutMapping
    fun updateNew(@RequestBody new: New) = newService.updateNew(new)

    @DeleteMapping("/{id}")
    fun deleteNew(@PathVariable("id") id: Long) = newService.deleteNew(id)
}