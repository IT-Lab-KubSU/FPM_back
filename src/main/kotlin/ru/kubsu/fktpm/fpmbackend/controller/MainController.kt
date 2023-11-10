package ru.kubsu.fktpm.fpmbackend.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {

    @GetMapping("/")
    fun index() = "/index.html"

    @GetMapping("/log-in")
    fun login() = "/login.html"
}