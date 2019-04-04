package br.com.ope.dto.v1

import br.com.ope.dto.DomainModelDTO
import io.swagger.annotations.ApiModel
import java.util.*

@ApiModel
data class CursoV1DTO(
        val sigla : String = "",
        val semestres : Int = 0,
        val disciplinas: MutableList<UUID> = mutableListOf(),
        val turmas: MutableList<UUID> = mutableListOf(),
        override val  id : UUID? = null
) : DomainModelDTO(id)