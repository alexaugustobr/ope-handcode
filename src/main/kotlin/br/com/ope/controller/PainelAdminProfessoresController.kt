package br.com.ope.controller

import br.com.ope.model.Curso
import br.com.ope.model.Professor
import br.com.ope.repository.ProfessorRepository
import br.com.ope.vo.MensagemVO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import javax.validation.Valid

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

    @PostMapping("/novo")
    fun novoSalvar(@Valid professor: Professor, bindingResult : BindingResult, model : Model, redirectAttributes: RedirectAttributes) : String {
        if (bindingResult.hasErrors()) return novo(professor,bindingResult,model)
        professor.ativo = true
        professorRepository.save(professor)
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Professor salvo!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin/professores"
    }

    @GetMapping("/{id}")
    fun editar(redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID) : String {

        var professor = professorRepository.findById(id)

        if (!professor.isPresent) return redirectProfessorNaoEncontrado(model, redirectAttributes)

        popularForm(model, professor.get())

        return "painel/admin/professores/editar"
    }

    @PostMapping("/{id}")
    fun editarSalvar(@Valid professorEditado : Professor, bindingResult: BindingResult, redirectAttributes: RedirectAttributes, model : Model,  @PathVariable id : UUID) : String {

        var professor = professorRepository.findById(id)

        if(!professor.isPresent) return redirectProfessorNaoEncontrado(model, redirectAttributes)

        if (bindingResult.hasErrors()) return "painel/admin/professores/editar"

        professorRepository.save(professor.get().atualizar(professorEditado))

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Professor salvo!","Sucesso!", MensagemVO.TipoMensagem.success ))

        return "redirect:/painel/admin/professores"

    }

    private fun redirectProfessorNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Professor não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/admin/professores"
    }

    private fun popularForm(model : Model, professor: Professor = Professor()) : Model {
        model.addAttribute("professor", professor)
        return model
    }
}