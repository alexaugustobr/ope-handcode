package br.com.ope.repository

import br.com.ope.model.Entrega
import br.com.ope.model.Grupo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EntregaRepository : JpaRepository<Entrega, UUID> {
    fun findAllByStatus(status: Entrega.Status) : MutableList<Entrega>
    fun findAllByStatusAndGrupoOrderByDataCriacaoDesc(entregue: Entrega.Status, grupo: Grupo): MutableList<Entrega>
}