package br.com.ope.model

import br.com.ope.enumx.Role
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
open class Professor : Usuario {

    constructor() : super()

    constructor(
            nome: String,
            email: String,
            ativo: Boolean,
            senha: String,
            permissoes: MutableSet<Role>
    ) : super(nome, email, ativo, senha, permissoes)

    override fun getPainelUrl() = "professor"
}