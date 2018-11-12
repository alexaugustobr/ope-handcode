package br.com.ope.controller

import br.com.ope.model.Entrega
import br.com.ope.model.Tarefa
import br.com.ope.model.Usuario
import br.com.ope.repository.*
import br.com.ope.service.TarefaService
import br.com.ope.vo.MensagemVO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/painel/professor/tarefas")
class PainelProfessorTarefasController {

    val grupoRepository: GrupoRepository
    val professorRepository: ProfessorRepository
    val entregaRepository: EntregaRepository
    val turmaRepository: TurmaRepository
    val cursoRepository: CursoRepository
    val disciplinaRepository: DisciplinaRepository
    val tarefaRepository: TarefaRepository
    val tarefaService: TarefaService

    constructor(grupoRepository: GrupoRepository, professorRepository: ProfessorRepository, entregaRepository: EntregaRepository, turmaRepository: TurmaRepository, cursoRepository: CursoRepository, disciplinaRepository: DisciplinaRepository, tarefaRepository: TarefaRepository, tarefaService: TarefaService) {
        this.grupoRepository = grupoRepository
        this.professorRepository = professorRepository
        this.entregaRepository = entregaRepository
        this.turmaRepository = turmaRepository
        this.cursoRepository = cursoRepository
        this.disciplinaRepository = disciplinaRepository
        this.tarefaRepository = tarefaRepository
        this.tarefaService = tarefaService
    }

    @GetMapping()
    fun index(model : Model, @AuthenticationPrincipal usuario: Usuario) : String {

        val entregasPendentes  = entregaRepository.findAllByStatusAndProfessorAvaliador_idOrderByTarefa_DataHoraDesc(Entrega.Status.REALIZADA , usuario.id)
        val entregasCorrigidas = entregaRepository.findAllByStatusAndProfessorAvaliador_idOrderByTarefa_DataHoraDesc(Entrega.Status.REALIZADA , usuario.id)

        val tarefaPendenteMap : MultiValueMap<Tarefa, Entrega> = LinkedMultiValueMap()
        val tarefaCorrigidaMap : MultiValueMap<Tarefa, Entrega> = LinkedMultiValueMap()

        for (entrega in entregasPendentes) {
            tarefaPendenteMap.add(entrega.tarefa!!, entrega)
        }

        for (entrega in entregasCorrigidas) {
            tarefaCorrigidaMap.add(entrega.tarefa!!, entrega)
        }

        model.addAttribute("tarefaPendenteMap", tarefaPendenteMap)
        model.addAttribute("tarefaCorrigidaMap", tarefaCorrigidaMap)
        return "painel/professor/tarefas/index"
    }

    private fun redirectTarefaNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/professor/tarefas"
    }

}