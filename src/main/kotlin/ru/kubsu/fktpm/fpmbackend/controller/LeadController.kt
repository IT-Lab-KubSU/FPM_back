package ru.kubsu.fktpm.fpmbackend.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.Lead
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.LeadFilter
import ru.kubsu.fktpm.fpmbackend.service.lead.dto.LeadRequest
import ru.kubsu.fktpm.fpmbackend.service.lead.LeadService

@Tag(name = "Leads", description = "Leads endpoints")
@CrossOrigin
@RestController
@RequestMapping("/api/leads")
class LeadController(@Autowired val leadService: LeadService) {
    @PostMapping
    fun createLead(@RequestBody lead: LeadRequest) = leadService.create(lead)

    @GetMapping("/{id}")
    fun getLeadById(@PathVariable("id") id: Long) = leadService.getById(id)

    @PostMapping("/filter")
    fun getLeads(
        @RequestBody filter: LeadFilter
    ) = leadService.getWithFilter(filter)

    @PutMapping
    fun updateLead(@RequestBody lead: Lead) = leadService.update(lead)

    @DeleteMapping("/{id}")
    fun deleteLead(@PathVariable("id") id: Long) = leadService.deleteById(id)

    @DeleteMapping
    fun deleteLeads(@RequestBody ids: List<Long>) = leadService.deleteByIds(ids)
}