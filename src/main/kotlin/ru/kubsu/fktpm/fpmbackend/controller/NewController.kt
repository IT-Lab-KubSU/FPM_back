package ru.kubsu.fktpm.fpmbackend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.kubsu.fktpm.fpmbackend.service.news.NewService
import ru.kubsu.fktpm.fpmbackend.service.news.dto.NewRequest
import ru.kubsu.fktpm.fpmbackend.service.news.dto.New
import ru.kubsu.fktpm.fpmbackend.service.news.dto.NewsFilter

@Tag(name = "News", description = "News endpoints")
@CrossOrigin
@RestController
@RequestMapping("/api/news")
class NewController(@Autowired val newService: NewService) {
    @PostMapping
    fun createNew(@RequestBody new: NewRequest) = newService.create(new)

    @GetMapping("/{id}")
    fun getNewById(@PathVariable("id") id: Long) = newService.getById(id)

    @PostMapping("/filter")
    fun getNews(
        @RequestBody filter: NewsFilter
    ) = newService.getWithFilter(filter)

    @PutMapping
    fun updateNew(@RequestBody new: New) = newService.update(new)

    @DeleteMapping("/{id}")
    fun deleteNew(@PathVariable("id") id: Long) = newService.deleteById(id)

    @DeleteMapping
    fun deleteNews(@RequestBody ids: List<Long>) = newService.deleteByIds(ids)
}