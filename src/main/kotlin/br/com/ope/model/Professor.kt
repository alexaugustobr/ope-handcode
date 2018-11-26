package br.com.ope.model

import br.com.ope.enumx.Role
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
open class Professor : Usuario {

    fun atualizar(professor: Professor) : Professor {
        this.nome = professor.nome
        this.email = professor.email
        return this
    }

    constructor() : super()
    constructor(id: UUID?) : super(id)
    constructor(
            nome: String,
            email: String,
            ativo: Boolean,
            senha: String,
            permissoes: MutableSet<Role>

    ) : super(nome, email, ativo, senha, permissoes) {
            this.nome = nome
            this.email = email
            this.ativo = ativo
            this.senha = senha
            this.permissoes = permissoes
    }
}