package br.com.ope.model

import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.Entity
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.validation.constraints.NotNull

@Entity
open class Evento : AbstractModel {

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @NotNull
    var dataHora : Date? = null
    var descricao : String = ""
    var titulo : String = ""

    @ManyToMany
    @JoinTable
    var disciplinas : MutableList<Disciplina> = mutableListOf()

    @ManyToMany
    @JoinTable
    var cursos : MutableList<Curso> = mutableListOf()

    @ManyToMany
    @JoinTable
    var turmas : MutableList<Turma> = mutableListOf()

    @ManyToMany
    @JoinTable
    var arquivos: MutableList<Arquivo> = mutableListOf()

    constructor() : super()
    constructor(dataHora: Date, descricao: String, titulo: String, disciplinas: MutableList<Disciplina>, cursos: MutableList<Curso>, turmas: MutableList<Turma>) : super() {
        this.dataHora = dataHora
        this.descricao = descricao
        this.titulo = titulo
        this.disciplinas = disciplinas
        this.cursos = cursos
        this.turmas = turmas
    }

    open fun atualizar(evento: Evento): Evento {
        this.dataHora = evento.dataHora
        this.descricao = evento.descricao
        this.titulo = evento.titulo
        this.cursos = evento.cursos
        this.turmas = evento.turmas
        this.disciplinas = evento.disciplinas
        return this
    }

}