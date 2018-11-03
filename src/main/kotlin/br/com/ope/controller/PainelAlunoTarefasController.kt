package br.com.ope.controller

import br.com.ope.model.Entrega
import br.com.ope.model.Usuario
import br.com.ope.repository.*
import br.com.ope.service.TarefaService
import br.com.ope.vo.MensagemVO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/painel/aluno/tarefas")
class PainelAlunoTarefasController {

    val grupoRepository: GrupoRepository
    val alunoRepository: AlunoRepository
    val entregaRepository: EntregaRepository
    val turmaRepository: TurmaRepository
    val cursoRepository: CursoRepository
    val disciplinaRepository: DisciplinaRepository
    val tarefaRepository: TarefaRepository
    val tarefaService: TarefaService

    constructor(grupoRepository: GrupoRepository, alunoRepository: AlunoRepository, entregaRepository: EntregaRepository, turmaRepository: TurmaRepository, cursoRepository: CursoRepository, disciplinaRepository: DisciplinaRepository, tarefaRepository: TarefaRepository, tarefaService: TarefaService) {
        this.grupoRepository = grupoRepository
        this.alunoRepository = alunoRepository
        this.entregaRepository = entregaRepository
        this.turmaRepository = turmaRepository
        this.cursoRepository = cursoRepository
        this.disciplinaRepository = disciplinaRepository
        this.tarefaRepository = tarefaRepository
        this.tarefaService = tarefaService
    }


    @GetMapping()
    fun index(model : Model, @AuthenticationPrincipal usuario: Usuario) : String {

        val aluno = alunoRepository.findOneByEmail(usuario.email).orElseThrow { RuntimeException() }

        val grupo = grupoRepository.findOneByAlunosIn(Arrays.asList(aluno)).orElseThrow { RuntimeException() }

        val entregasPendentes  = entregaRepository.findAllByStatusAndGrupoOrderByTarefa_DataHoraDesc(Entrega.Status.PENDENTE , grupo)
        val entregasRealizadas = entregaRepository.findAllByStatusAndGrupoOrderByTarefa_DataHoraDesc(Entrega.Status.REALIZADA , grupo)

        model.addAttribute("grupo", grupo)
        model.addAttribute("entregasPendentes", entregasPendentes)
        model.addAttribute("entregasRealizadas", entregasRealizadas)
        return "painel/aluno/tarefas/index"
    }

    @GetMapping("/{id}")
    fun detalhe(redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID) : String {
        val tarefa = tarefaRepository.findById(id)

        if (!tarefa.isPresent) return redirectTarefaNaoEncontrado(model, redirectAttributes)

        model.addAttribute("tarefa", tarefa.get())
        model.addAttribute("turmas", turmaRepository.findAll())
        model.addAttribute("disciplinas", disciplinaRepository.findAll())
        model.addAttribute("cursos", cursoRepository.findAll())
        return "painel/aluno/tarefas/detalhe"
    }

    private fun redirectTarefaNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/aluno/agenda"
    }

}