package br.com.ope.controller

import br.com.ope.model.Professor
import br.com.ope.repository.ProfessorRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/painel/admin/professores")
class PainelAdminProfessoresController {
    private val professorRepository : ProfessorRepository

    constructor(professorRepository: ProfessorRepository){
        this.professorRepository = professorRepository
    }

    @GetMapping
    fun index(model : Model) : String {
        model.addAttribute("professores", professorRepository.findAllByAtivoIsTrue())
        return "painel/admin/professores/index"
    }

    @GetMapping("/novo")
    fun novo(professor: Professor, bindingResult: BindingResult, model: Model) : String {
        popularForm(model, professor)
        return "/painel/admin/professores/novo"
    }

    private fun popularForm(model : Model, professor: Professor = Professor()) : Model {
        model.addAttribute("professor", professor)
        return model
    }
}