package br.com.ope.dto.v1

import br.com.ope.dto.DomainModelDTO
import io.swagger.annotations.ApiModel
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel
class GrupoCadastroV1DTO(
        override val id : UUID? = null,
        @field:NotNull
        val turma : UUID,
        val logoHash : UUID? = null,
        @field:NotBlank
        val nome : String,
        @field:NotBlank
        val tema : String,
        val alunos : List<AlunoCadastroV1DTO>
) : DomainModelDTO(id)