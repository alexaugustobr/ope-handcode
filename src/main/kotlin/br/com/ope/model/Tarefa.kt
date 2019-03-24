package br.com.ope.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Tarefa : Evento {

    @OneToMany(mappedBy = "tarefa")
    @JsonIgnore
    var entregas : MutableList<Entrega> = mutableListOf()

    fun getDataEntrega() = super.dataHora

    constructor() : super()
    constructor(data: Date, descricao: String, titulo: String, cursos: MutableList<Curso>, turmas: MutableList<Turma>, entregas: MutableList<Entrega>) :
            super(data, descricao, titulo, cursos, turmas) {
        this.entregas = entregas
    }

    fun atualizar(evento: Tarefa): Tarefa {
        super.atualizar(evento)
        this.entregas = evento.entregas
        return this
    }


}