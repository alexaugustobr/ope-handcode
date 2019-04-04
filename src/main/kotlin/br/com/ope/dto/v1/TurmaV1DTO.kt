package br.com.ope.dto.v1

import br.com.ope.dto.DomainModelDTO
import br.com.ope.model.Turma
import io.swagger.annotations.ApiModel
import java.time.Year
import java.util.*

@ApiModel
class TurmaV1DTO(
        val letra : String = "",
        val semestre : Int = 1,
        val ano : Int = Year.now().value,
        val curso : UUID? = null,
        val alunos: MutableList<UUID> = mutableListOf(),
        val periodo : Turma.Periodo? = null,
        val grupos: MutableList<UUID> = mutableListOf(),
        val eventos: MutableList<UUID> = mutableListOf(),
        val disciplina : UUID? = null,
        val tarefas : MutableList<UUID> = mutableListOf(),
        override val  id : UUID? = null
) : DomainModelDTO(id)