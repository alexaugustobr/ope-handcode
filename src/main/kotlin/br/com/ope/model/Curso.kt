package br.com.ope.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jetbrains.annotations.NotNull
import java.util.*
import javax.persistence.Entity
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.validation.constraints.NotBlank

@Entity
class Curso : AbstractModel {

    @NotBlank
    var nome : String = ""
    @NotBlank
    var sigla : String = ""
    @NotNull
    var semestres : Int = 4
    @ManyToMany
    @JoinTable
    @JsonIgnore
    var disciplinas: List<Disciplina> = mutableListOf()

    @OneToMany(mappedBy = "curso")
    @JsonIgnore
    var grupos: List<Grupo> = mutableListOf()

    @OneToMany(mappedBy = "curso")
    @JsonIgnore
    var tarefas : List<Tarefa> = mutableListOf()

    fun atualizar(curso: Curso): Curso {
        this.nome = curso.nome
        this.sigla = curso.sigla
        this.semestres = curso.semestres
        this.disciplinas = curso.disciplinas
        return this
    }

    constructor() : super()

    constructor(id: UUID?) : super(id)
    constructor(nome: String, sigla: String, semestres: Int, disciplinas : List<Disciplina>  = mutableListOf()) : super() {
        this.nome = nome
        this.sigla = sigla
        this.semestres = semestres
        this.disciplinas = disciplinas
    }


}