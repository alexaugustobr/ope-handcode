package br.com.ope.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.OneToMany

@Entity
class Disciplina : AbstractModel {

    var nome : String = ""
    var sigla  : String = ""
    var semestre : Int = 1
    @ManyToMany(mappedBy = "disciplinas")
    @JsonIgnore
    var cursos: MutableList<Curso> = mutableListOf()

    @ManyToMany(mappedBy = "disciplinas")
    @JsonIgnore
    var tarefas: MutableList<Tarefa> = mutableListOf()

    @OneToMany(mappedBy = "disciplina")
    @JsonIgnore
    var entregas: MutableList<Entrega> = mutableListOf()

    @OneToMany(mappedBy = "disciplina")
    @JsonIgnore
    var turmas: MutableList<Turma> = mutableListOf()

    constructor() : super()

    constructor(id: UUID?) : super(id)
    constructor(nome: String, sigla: String) : super() {
        this.nome = nome
        this.sigla = sigla
    }


}