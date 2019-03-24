package br.com.ope.model

import br.com.ope.enumx.Role
import javax.persistence.Entity

@Entity
open class Administrador : Usuario {

    constructor() : super()

    constructor(nome: String, email: String, ativo: Boolean, senha: String, permissoes: MutableSet<Role>) : super(nome, email, ativo, senha, permissoes)

    fun parse(usuario: Administrador): Administrador {
        return super.parse(usuario) as Administrador
    }

    override fun parse(usuario: Usuario): Usuario {
        return this.parse(usuario as Administrador)
    }


}