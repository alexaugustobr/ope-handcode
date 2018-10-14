package br.com.ope.model

import br.com.ope.enumx.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated


@Entity
open class Usuario : AbstractModel, UserDetails {

    var nome  : String = ""
    var email : String = ""
    var ativo : Boolean = false
    @JsonIgnore
    var senha : String = ""
    @ElementCollection(targetClass = Role::class)
    @Enumerated(EnumType.STRING)
    var permissoes: MutableSet<Role> = mutableSetOf()

    constructor() : super()

    constructor(nome: String) : super() {
        this.nome = nome
    }

    constructor(id: UUID? = null, nome: String) : super(id) {
        this.nome = nome
    }

    constructor(nome: String, email: String, ativo: Boolean, senha: String, permissoes: MutableSet<Role>) : super() {
        this.nome = nome
        this.email = email
        this.ativo = ativo
        this.senha = senha
        this.permissoes = permissoes
    }

    constructor(email: String, authorities: MutableSet<GrantedAuthority>) : super() {
        this.email = email
        for (authority in authorities) {
            permissoes.add(Role.valueOf(authority.authority))
        }
    }


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableSetOf<GrantedAuthority>()

        for (permissao in permissoes) {
            authorities.add(SimpleGrantedAuthority(permissao.name))
        }

        return authorities
    }
    @JsonIgnore
    override fun isEnabled(): Boolean {
        return ativo
    }
    @JsonIgnore
    override fun getUsername(): String {
        return email
    }
    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return ativo
    }
    @JsonIgnore
    override fun getPassword(): String {
        return senha
    }
    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return ativo
    }
    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return ativo
    }

    open fun getPainelUrl() = "painel"

}