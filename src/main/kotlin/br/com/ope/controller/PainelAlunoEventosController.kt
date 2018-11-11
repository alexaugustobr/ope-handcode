package br.com.ope.controller

import br.com.ope.repository.*
import br.com.ope.service.TarefaService
import br.com.ope.vo.MensagemVO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/painel/aluno/eventos")
class PainelAlunoEventosController {

    val grupoRepository: GrupoRepository
    val alunoRepository: AlunoRepository
    val entregaRepository: EntregaRepository
    val turmaRepository: TurmaRepository
    val cursoRepository: CursoRepository
    val disciplinaRepository: DisciplinaRepository
    val eventoRepository: EventoRepository
    val tarefaService: TarefaService

    constructor(grupoRepository: GrupoRepository, alunoRepository: AlunoRepository, entregaRepository: EntregaRepository, turmaRepository: TurmaRepository, cursoRepository: CursoRepository, disciplinaRepository: DisciplinaRepository, eventoRepository: EventoRepository, tarefaService: TarefaService) {
        this.grupoRepository = grupoRepository
        this.alunoRepository = alunoRepository
        this.entregaRepository = entregaRepository
        this.turmaRepository = turmaRepository
        this.cursoRepository = cursoRepository
        this.disciplinaRepository = disciplinaRepository
        this.eventoRepository = eventoRepository
        this.tarefaService = tarefaService
    }

    @GetMapping("/{id}")
    fun detalhe(redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID) : String {
        val evento = eventoRepository.findById(id)

        if (!evento.isPresent) return redirectEventoNaoEncontrado(model, redirectAttributes)

        model.addAttribute("evento", evento.get())
        model.addAttribute("turmas", turmaRepository.findAll())
        model.addAttribute("disciplinas", disciplinaRepository.findAll())
        model.addAttribute("cursos", cursoRepository.findAll())
        return "painel/aluno/eventos/detalhe"
    }

    private fun redirectEventoNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Evento não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/aluno/agenda"
    }

}