package br.com.ope.model

import br.com.ope.dto.v1.GrupoCadastroV1DTO
import br.com.ope.exception.BusinessException
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Entity
class Grupo : DomainModel {

    @NotBlank
    var nome : String = ""
    @NotBlank
    var tema : String = ""

    @OneToMany(mappedBy = "grupo")
    @JsonIgnore
    @Valid
    var alunos : MutableList<Aluno> = mutableListOf()

    @ManyToMany
    @JoinTable
    @JsonIgnore
    var alunosRemovidos : MutableList<Aluno> = mutableListOf()

    @Enumerated(EnumType.STRING)
    var status : Status = Status.AGUARDANDO

    var logoHash : UUID? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var turma : Turma? = null

    @ManyToMany
    @JoinTable
    @JsonIgnore
    var turmasAnteriores : MutableList<Turma> = mutableListOf()

    @OneToMany(mappedBy = "grupo")
    var entregas : MutableList<Entrega> = mutableListOf()

    constructor() : super()

    constructor(id: UUID? = null,
                nome: String,
                alunos: MutableList<Aluno> = mutableListOf(),
                alunosRemovidos: MutableList<Aluno> = mutableListOf(),
                status: Status = Grupo.Status.AGUARDANDO,
                turma : Turma,
                logoHash: UUID? = null,
                tema : String = "") : super(id) {
        this.nome = nome
        this.alunos = alunos
        this.alunosRemovidos = alunosRemovidos
        this.turma = turma
        this.tema = tema
        this.status = status
        this.logoHash = logoHash
    }

    constructor(id: UUID? = null) : super(id)

    constructor(id: UUID? = null, nome: String, status: Status = Grupo.Status.AGUARDANDO, logoHash: UUID?, turma: Turma?, tema : String = "") : super(id) {
        this.nome = nome
        this.status = status
        this.logoHash = logoHash
        this.turma = turma
        this.status = status
        this.tema = tema
    }

    enum class Status(val nome : String, val cor : String) {

        APROVADO("Aprovado", "success"),
        RECUSADO("Recusado", "danger"),
        AGUARDANDO("Aguardando", "warning")

    }

    fun isAprovado() = Status.APROVADO == status

    fun isNotAprovado() = !isAprovado()

    fun isAguardando() = Status.AGUARDANDO == status

    @Throws(BusinessException::class)
    fun removerAluno(aluno: Aluno) {
        //if is valido

        if (!alunos.contains(aluno)) {
            throw BusinessException("Aluno não está no grupo!")
        }

        if (aluno.grupo != this) {
            throw BusinessException("Aluno não está no grupo!")
        }

        alunos.remove(aluno)
        alunosRemovidos.remove(aluno)
        aluno.grupo = null
    }

    companion object {
        fun criarNovoGrupo(dto : GrupoCadastroV1DTO) : Grupo {
            val alunos : MutableList<Aluno> = mutableListOf<Aluno>()

            for (alunoDto in dto.alunos) {
                alunos.add(Aluno.criarNovo(alunoDto, dto))
            }

            val grupo = Grupo(
                    nome = dto.nome,
                    turma = Turma(dto.turma),
                    tema = dto.tema,
                    logoHash = dto.logoHash,
                    alunos = alunos
            )
            return grupo
        }
    }

}