package br.com.ope.controller

import br.com.ope.repository.ProfessorRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/painel/admin/professor")
class PainelProfessorController {

    private val professorRepository : ProfessorRepository

    constructor(professorRepository: ProfessorRepository){
        this.professorRepository = professorRepository
    }

    @GetMapping("/painel/professor")
    fun index(model : Model) : String {

        return "painel/professor/index"
    }

}