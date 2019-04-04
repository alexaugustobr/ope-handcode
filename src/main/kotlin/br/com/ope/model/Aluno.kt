package br.com.ope.model

import br.com.ope.dto.v1.AlunoCadastroV1DTO
import br.com.ope.dto.v1.GrupoCadastroV1DTO
import br.com.ope.enumx.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Aluno : Usuario {

    @Column(unique=true)
    @NotNull(message = "RA é obrigatório.")
    var ra : Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonIgnore
    var grupo : Grupo? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var turma: Turma? = null

    var fotoHash : UUID? = null

    var telefone : String? = null

    constructor() : super()

    constructor(id: UUID?) : super(id)

    constructor(nome: String = "",
                email: String = "",
                ativo: Boolean = false,
                senha: String = "",
                telefone : String? = null,
                permissoes: MutableSet<Role> = mutableSetOf(Role.ROLE_ALUNO),
                ra: Long = 0,
                grupo: Grupo? = null,
                turma : Turma? = null) : super(nome, email, ativo, senha, permissoes) {
        this.ra = ra
        this.grupo = grupo
        this.turma = turma
        this.telefone = telefone
    }

    override fun getPainelUrl() = "aluno"

    companion object {
        fun criarNovo(dto: AlunoCadastroV1DTO, grupoDto: GrupoCadastroV1DTO) : Aluno {
            return Aluno(
                    nome = dto.nome,
                    telefone = dto.telefone,
                    email = dto.email,
                    ra = dto.ra,
                    turma = Turma(grupoDto.turma),
                    grupo = Grupo(grupoDto.id)
            )
        }
    }
}