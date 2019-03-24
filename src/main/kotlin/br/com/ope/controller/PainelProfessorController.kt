package br.com.ope.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/painel/professor")
class PainelProfessorController {

    @GetMapping()
    fun index(model : Model) : String {

        return "painel/professor/index"
    }

}