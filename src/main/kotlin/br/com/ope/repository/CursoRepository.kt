package br.com.ope.repository

import br.com.ope.model.Curso
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CursoRepository : JpaRepository<Curso, UUID> {
    fun findAllByExcluidoIsFalse(): MutableList<Curso>
    fun findAllByOrderByNome(): MutableList<Curso>
}