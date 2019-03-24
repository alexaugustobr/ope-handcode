package br.com.ope.repository

import br.com.ope.model.Aluno
import br.com.ope.model.Grupo
import br.com.ope.model.Tarefa
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GrupoRepository : JpaRepository<Grupo, UUID> {
    fun findAllByAlunos_idIn(asList: MutableList<UUID?>): Any
    fun findAllByTurma_Tarefas(tarefa: Tarefa): Any
    fun findAllByTurma_Disciplina_IdIn(disciplinas: MutableList<UUID>): MutableList<Grupo>
    fun findOneByAlunosIn(asList: List<Aluno>): Optional<Grupo>
}