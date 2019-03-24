package br.com.ope.controller.rest

import br.com.ope.model.Turma
import br.com.ope.repository.TurmaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(value = ["/rest/turmas", "/api/v1/turmas"])
class TurmaRestController {

    private val turmaRepository : TurmaRepository

    constructor(turmaRepository: TurmaRepository) {
        this.turmaRepository = turmaRepository
    }

    @GetMapping
    fun turmas (@RequestParam("cursoId") cursoId : List<UUID>) :  MutableList<Turma> {
        if (cursoId.isEmpty()) return mutableListOf()
        return turmaRepository.findAllByCurso_idInOrderBySemestreDesc(cursoId)
    }

}