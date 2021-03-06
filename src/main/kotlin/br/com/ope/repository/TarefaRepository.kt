package br.com.ope.repository

import br.com.ope.model.Tarefa
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TarefaRepository : JpaRepository<Tarefa, UUID>