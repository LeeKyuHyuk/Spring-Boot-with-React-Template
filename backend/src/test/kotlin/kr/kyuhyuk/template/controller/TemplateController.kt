package kr.kyuhyuk.template.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TemplateController {
    @GetMapping("/")
    fun template(): String {
        return "index.html";
    }
}