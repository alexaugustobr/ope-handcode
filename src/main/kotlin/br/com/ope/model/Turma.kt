package br.com.ope.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Year
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Turma : DomainModel {

    @NotBlank
    var letra : String = ""

    @NotNull
    var semestre : Int = 1

    @NotNull
    var ano : Int = Year.now().value

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @NotNull
    var curso : Curso? = null

    @OneToMany(mappedBy = "turma")
    @JsonIgnore
    var alunos: MutableList<Aluno> = mutableListOf()

    @Enumerated(EnumType.STRING)
    @NotNull
    var periodo : Periodo? = null

    @OneToMany(mappedBy = "turma")
    @JsonIgnore
    var grupos: MutableList<Grupo> = mutableListOf()

    @ManyToMany(mappedBy = "turmas")
    @JsonIgnore
    var eventos: MutableList<Evento> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var disciplina : Disciplina? = null

    @ManyToMany(mappedBy = "turmas")
    var tarefas : MutableList<Tarefa> = mutableListOf()

    constructor() : super()

    constructor(id: UUID?) : super(id)

    constructor(id: UUID?, letra: String, semestre: Int, ano: Int, curso: Curso?) : super(id) {
        this.letra = letra
        this.semestre = semestre
        this.ano = ano
        this.curso = curso
    }

    constructor(letra: String, semestre: Int, ano: Int, curso: Curso?,
                alunos: MutableList<Aluno>, periodo: Periodo?, disciplina : Disciplina) : super() {
        this.letra = letra
        this.semestre = semestre
        this.ano = ano
        this.curso = curso
        this.alunos = alunos
        this.periodo = periodo
        this.disciplina = disciplina
    }

    fun atualizar(turma: Turma) : Turma {
        this.semestre = turma.semestre
        this.ano = turma.ano
        this.curso = turma.curso
        this.periodo = turma.periodo
        this.disciplina = turma.disciplina
        return this
    }

    enum class Periodo(val nome : String) {

        MANHA("Manhã"),
        TARDE("Tarde"),
        NOITE("Noite")

    }

}
