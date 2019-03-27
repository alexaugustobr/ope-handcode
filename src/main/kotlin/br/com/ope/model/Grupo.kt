package br.com.ope.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Entity
class Grupo : AbstractModel {

    @NotBlank
    var nome : String = ""
    @NotBlank
    var tema : String = ""
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    var curso : Curso? = null

    @OneToMany(mappedBy = "grupo")
    @JsonIgnore
    @Valid
    var alunos : MutableList<Aluno> = mutableListOf()

    @ManyToMany
    @JoinTable
    var alunosRemovidos : MutableList<Aluno> = mutableListOf()

    @Enumerated(EnumType.STRING)
    var status : Status = Status.AGUARDANDO

    var logoHash : UUID? = null

    @ManyToOne
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
                curso: Curso?,
                alunos: MutableList<Aluno> = mutableListOf(),
                alunosRemovidos: MutableList<Aluno> = mutableListOf(),
                turma : Turma,
                tema : String) : super(id) {
        this.nome = nome
        this.curso = curso
        this.alunos = alunos
        this.alunosRemovidos = alunosRemovidos
        this.turma = turma
        this.tema = tema
    }

    constructor(id: UUID?) : super(id)

    constructor(id: UUID?, nome: String, status: Status, logoHash: UUID?, turma: Turma?) : super(id) {
        this.nome = nome
        this.status = status
        this.logoHash = logoHash
        this.turma = turma
    }

    enum class Status(val nome : String, val cor : String) {

        APROVADO("Aprovado", "success"),
        RECUSADO("Recusado", "danger"),
        AGUARDANDO("Aguardando", "warning")

    }

    fun isAprovado() = Status.APROVADO == status

    fun isNotAprovado() = !isAprovado()

    fun isAguardando() = Status.AGUARDANDO == status

}