package br.com.ope.controller

import br.com.ope.model.Curso
import br.com.ope.repository.CursoRepository
import br.com.ope.repository.DisciplinaRepository
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
@RequestMapping("/painel/admin/cursos")
class PainelAdminCursosController {

    private val cursoRepository : CursoRepository
    private val disciplinaRepository : DisciplinaRepository

    constructor(cursoRepository: CursoRepository, disciplinaRepository: DisciplinaRepository) {
        this.cursoRepository = cursoRepository
        this.disciplinaRepository = disciplinaRepository
    }

    @GetMapping
    fun index(model : Model) : String {
        model.addAttribute("cursos", cursoRepository.findAllByExcluidoIsFalse())
        return "painel/admin/cursos/index"
    }

    @GetMapping("/novo")
    fun novo(curso: Curso, bindingResult : BindingResult, model : Model) : String {
        popularForm(model, curso)
        return "painel/admin/cursos/novo"
    }

    @PostMapping("/novo")
    fun novoSalvar(@Valid curso: Curso, bindingResult : BindingResult, model : Model, redirectAttributes: RedirectAttributes) : String {
        if (bindingResult.hasErrors()) return novo(curso,bindingResult,model)
        cursoRepository.save(curso)
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Curso salvo!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin/cursos"
    }

    @GetMapping("/{id}")
    fun editar(redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID) : String {
        var curso = cursoRepository.findById(id)

        if (!curso.isPresent) return redirectCursoNaoEncontrado(model, redirectAttributes)

        popularForm(model, curso.get())
        return "painel/admin/cursos/editar"
    }

    @PostMapping("/{id}")
    fun editarSalvar(@Valid cursoEditado: Curso, bindingResult : BindingResult, redirectAttributes: RedirectAttributes, model : Model,  @PathVariable id : UUID) : String {
        var curso = cursoRepository.findById(id)

        if (!curso.isPresent) return redirectCursoNaoEncontrado(model, redirectAttributes)

        if (bindingResult.hasErrors()) return "painel/admin/cursos/editar"
        cursoRepository.save(curso.get().atualizar(cursoEditado))

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Curso salvo!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin/cursos"
    }

    @GetMapping("/{id}/excluir")
    fun excluir(model : Model, redirectAttributes: RedirectAttributes, @PathVariable id : UUID) : String {
        var curso = cursoRepository.findById(id)

        if (!curso.isPresent) return redirectCursoNaoEncontrado(model, redirectAttributes)

        curso.get().dataExclusao = Date()
        cursoRepository.save(curso.get())

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Curso excluido.","", MensagemVO.TipoMensagem.info ))
        return "redirect:/painel/admin/cursos"
    }

    private fun redirectCursoNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Curso não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/admin/cursos"
    }

    private fun popularForm(model : Model, curso : Curso = Curso()): Model {
        model.addAttribute("curso", curso)
        model.addAttribute("disciplinas", disciplinaRepository.findAll())
        return model
    }

}