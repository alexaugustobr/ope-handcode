package br.com.ope.dto.v1

import br.com.ope.dto.DomainModelDTO
import br.com.ope.model.Grupo
import io.swagger.annotations.ApiModel
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel
class GrupoCadastroV1DTO(
        override val id : UUID? = null,
        @field:NotNull
        val turma : UUID? = null,
        val logoHash : UUID? = null,
        @field:NotBlank
        val nome : String = "",
        @field:NotBlank
        val tema : String = "",
        val alunos : List<AlunoCadastroV1DTO> = mutableListOf()
) : DomainModelDTO(id) {
        companion object {
            fun fromDomain(grupo: Grupo): GrupoCadastroV1DTO {
                    val alunos = mutableListOf<AlunoCadastroV1DTO>()

                    for (aluno in grupo.alunos) {
                            alunos.add(AlunoCadastroV1DTO.fromDomain(aluno))
                    }

                    return GrupoCadastroV1DTO(
                            id = grupo.id,
                            nome = grupo.nome,
                            tema = grupo.tema,
                            logoHash = grupo.logoHash,
                            turma = grupo?.turma?.id,
                            alunos = alunos
                    )
            }
        }
}