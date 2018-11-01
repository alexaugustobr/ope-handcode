package br.com.ope.controller

import br.com.ope.model.Tarefa
import br.com.ope.repository.CursoRepository
import br.com.ope.repository.DisciplinaRepository
import br.com.ope.repository.TarefaRepository
import br.com.ope.repository.TurmaRepository
import br.com.ope.service.TarefaService
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
@RequestMapping("/painel/admin/tarefas")
class PainelAdminTarefasController {

    val turmaRepository: TurmaRepository
    val cursoRepository: CursoRepository
    val disciplinaRepository: DisciplinaRepository
    val tarefaRepository: TarefaRepository
    val tarefaService: TarefaService

    constructor(turmaRepository: TurmaRepository, cursoRepository: CursoRepository, disciplinaRepository: DisciplinaRepository, tarefaRepository: TarefaRepository, tarefaService: TarefaService) {
        this.turmaRepository = turmaRepository
        this.cursoRepository = cursoRepository
        this.disciplinaRepository = disciplinaRepository
        this.tarefaRepository = tarefaRepository
        this.tarefaService = tarefaService
    }

    @GetMapping("/novo")
    fun novo(model : Model, tarefa: Tarefa) : String {
        model.addAttribute("tarefa", tarefa)
        model.addAttribute("turmas", turmaRepository.findAll())
        model.addAttribute("disciplinas", disciplinaRepository.findAll())
        model.addAttribute("cursos", cursoRepository.findAll())
        return "painel/admin/tarefas/novo"
    }

    @PostMapping("/novo")
    fun novoSalvar(model: Model, @Valid tarefa : Tarefa, bindingResult: BindingResult, redirectAttributes: RedirectAttributes) : String {

        if (bindingResult.hasErrors()) return this.novo(model, tarefa)

        tarefaService.criarNovaTarefaEGerarEntregas(tarefa)

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa salvo!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin"
    }


    @GetMapping("/{id}")
    fun editar(redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID) : String {
        val tarefa = tarefaRepository.findById(id)

        if (!tarefa.isPresent) return redirectTarefaNaoEncontrado(model, redirectAttributes)

        model.addAttribute("tarefa", tarefa.get())
        model.addAttribute("turmas", turmaRepository.findAll())
        model.addAttribute("disciplinas", disciplinaRepository.findAll())
        model.addAttribute("cursos", cursoRepository.findAll())
        return "painel/admin/tarefas/editar"
    }

    @PostMapping("/{id}")
    fun editarSalvar(@Valid tarefaEditado: Tarefa, bindingResult : BindingResult, redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID) : String {
        val tarefa = tarefaRepository.findById(id)

        if (!tarefa.isPresent) return redirectTarefaNaoEncontrado(model, redirectAttributes)

        if (bindingResult.hasErrors()) return "painel/admin/tarefas/editar"

        tarefaRepository.save(tarefa.get().atualizar(tarefaEditado))

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa salvo!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/admin"
    }

    private fun redirectTarefaNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/admin"
    }


}