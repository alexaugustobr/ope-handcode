package br.com.ope.model

import br.com.ope.enumx.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

@Entity
class Aluno : Usuario {

    var ra : Long = 0

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    var grupo : Grupo? = null

    @ManyToOne
    @JoinColumn
    var turma: Turma? = null

    var fotoHash : UUID? = null

    @ManyToMany(mappedBy = "alunosRemovidos")
    @JsonIgnore
    var gruposRemovidos : MutableList<Grupo> = mutableListOf()

    constructor() : super()

    constructor(nome: String = "",
                email: String = "",
                ativo: Boolean = false,
                senha: String = "",
                permissoes: MutableSet<Role> = mutableSetOf(),
                ra: Long = 0,
                grupo: Grupo? = null,
                gruposRemovidos: MutableList<Grupo> = mutableListOf(), turma : Turma) : super(nome, email, ativo, senha, permissoes) {
        this.ra = ra
        this.grupo = grupo
        this.gruposRemovidos = gruposRemovidos
        this.turma = turma
    }

    override fun getPainelUrl() = "aluno"
}