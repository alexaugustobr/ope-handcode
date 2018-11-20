package br.com.ope.repository

import br.com.ope.model.Entrega
import br.com.ope.model.Grupo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EntregaRepository : JpaRepository<Entrega, UUID> {
    fun findAllByStatus(status: Entrega.Status) : MutableList<Entrega>
    fun findAllByStatusAndGrupoOrderByTarefa_DataHoraDesc(entregue: Entrega.Status, grupo: Grupo): MutableList<Entrega>
    fun findAllByStatusAndGrupoAndNotaNotNullOrderByTarefa_DataHoraDesc(realizada: Entrega.Status, grupo: Grupo?): MutableList<Entrega>
    fun findAllByStatusAndProfessorAvaliador_idAndNotaIsNullOrderByTarefa_DataHoraDesc(realizada: Entrega.Status, id: UUID?): MutableList<Entrega>
    fun findAllByStatusAndProfessorAvaliador_idAndNotaIsNotNullOrderByTarefa_DataHoraDesc(realizada: Entrega.Status, id: UUID?): MutableList<Entrega>
}