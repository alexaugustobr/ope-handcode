package br.com.ope.repository

import br.com.ope.model.Turma
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
interface TurmaRepository : JpaRepository<Turma, UUID> {
    fun findAllByCurso_idInAndExcluidoIsFalseOrderBySemestreDesc(cursoId: List<UUID>): MutableList<Turma>
    fun findAllByCurso_idIn(cursoIdList: MutableList<UUID>): MutableList<Turma>
}