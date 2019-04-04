package br.com.ope.dto.v1

import br.com.ope.dto.DomainModelDTO
import br.com.ope.model.Aluno
import io.swagger.annotations.ApiModel
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel
class AlunoCadastroV1DTO(
        override val id : UUID? = null,
        @field:NotNull
        val ra : Long = 0,
        val fotoHash : UUID? = null,
        val telefone : String? = null,
        @field:NotBlank
        val nome : String,
        @field:NotBlank
        val email : String
) : DomainModelDTO(id) {

    companion object {
        fun fromDomain(aluno : Aluno) : AlunoCadastroV1DTO {
            return AlunoCadastroV1DTO(
                    ra = aluno.ra!!,
                    id = aluno.id!!,
                    fotoHash = aluno.fotoHash,
                    telefone = aluno.telefone,
                    nome = aluno.nome,
                    email = aluno.email
            )
        }
    }
}