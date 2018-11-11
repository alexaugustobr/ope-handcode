package br.com.ope.controller

import br.com.ope.model.Disciplina
import br.com.ope.model.Entrega
import br.com.ope.model.Usuario
import br.com.ope.repository.*
import br.com.ope.service.TarefaService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
@RequestMapping("/painel/aluno/boletim")
class PainelAlunoBoletimController {

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

        val entregasRealizadas = entregaRepository.findAllByStatusAndGrupoAndNotaNotNullOrderByTarefa_DataHoraDesc(Entrega.Status.REALIZADA , grupo)

        val disciplinas : MultiValueMap<Disciplina, Entrega> = LinkedMultiValueMap()

        for (entrega in entregasRealizadas) {
            disciplinas.add(entrega.disciplina!!, entrega)
        }

        model.addAttribute("disciplinasMap", disciplinas)
        return "painel/aluno/boletim/index"
    }

}