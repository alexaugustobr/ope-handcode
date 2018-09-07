package br.com.ope.repository

import br.com.ope.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UsuarioRepository : JpaRepository<Usuario, UUID> {

    @Query("FROM Usuario usuario LEFT JOIN FETCH usuario.permissoes WHERE usuario.email = :email")
    fun findOneWithPermissoesByEmail(@Param("email") email: String) : Optional<Usuario>
}