package br.com.ope.controller

import br.com.ope.repository.AlunoRepository
import br.com.ope.repository.EntregaRepository
import br.com.ope.repository.GrupoRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/painel/aluno/agenda")
class PainelAlunoAgendaController {

    val grupoRepository: GrupoRepository
    val alunoRepository: AlunoRepository
    val entregaRepository: EntregaRepository

    constructor(grupoRepository: GrupoRepository, alunoRepository: AlunoRepository, entregaRepository: EntregaRepository) {
        this.grupoRepository = grupoRepository
        this.alunoRepository = alunoRepository
        this.entregaRepository = entregaRepository
    }


    @GetMapping()
    fun index(model : Model) : String {

        return "painel/aluno/agenda/index"
    }

}