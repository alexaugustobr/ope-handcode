package br.com.ope.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
class Entrega : AbstractModel {

    var dataEnvio : Date? = null
    var status : Status = Status.PENDENTE
    var nota : BigDecimal? = null

    @ManyToOne
    @JoinColumn
    var professorAvaliador : Professor? = null

    @ManyToOne
    @JoinColumn
    var tarefa : Tarefa? = null

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    var grupo : Grupo? = null

    @ManyToMany
    @JoinTable
    var arquivos: MutableList<Arquivo> = mutableListOf()

    @ManyToOne
    @JoinColumn
    var disciplina: Disciplina? = null

    @JsonProperty("titulo")
    fun getTitulo() : String {
        return tarefa!!.titulo
    }

    @JsonProperty("descricao")
    fun getDescricao() : String {
        return tarefa!!.descricao
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    fun getDataEntrega() : Date? {
        return tarefa!!.dataHora
    }

    constructor() : super()

    constructor(disciplina: Disciplina,  dataEnvio: Date? = null, situacaoEntrega: Status = Status.PENDENTE, tarefa: Tarefa, grupo: Grupo, arquivos: MutableList<Arquivo> = mutableListOf()) : super() {
        this.disciplina = disciplina
        this.dataEnvio = dataEnvio
        this.status = situacaoEntrega
        this.tarefa = tarefa
        this.grupo = grupo
        this.arquivos = arquivos
    }


    enum class Status(val nome : String, val textClass: String) {

        PENDENTE("Pendente", "text-warning"),
        REALIZADA("Realizada","text-success"),

    }


}