package br.com.ope.controller

import br.com.ope.model.Usuario
import br.com.ope.repository.AlunoRepository
import br.com.ope.repository.GrupoRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
@RequestMapping("/painel/aluno")
class PainelAlunoController {

    val grupoRepository: GrupoRepository
    val alunoRepository: AlunoRepository

    constructor(grupoRepository: GrupoRepository, alunoRepository: AlunoRepository) {
        this.grupoRepository = grupoRepository
        this.alunoRepository = alunoRepository
    }


    @GetMapping()
    fun index(model : Model, @AuthenticationPrincipal usuario: Usuario) : String {

        val aluno = alunoRepository.findOneByEmail(usuario.email).orElseThrow { RuntimeException() }

        val grupo = grupoRepository.findOneByAlunosIn(Arrays.asList(aluno)).orElseThrow { RuntimeException() }

        model.addAttribute("grupo", grupo)
        return "painel/aluno/index"
    }

}