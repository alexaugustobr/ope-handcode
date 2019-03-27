package br.com.ope.repository

import br.com.ope.model.Grupo
import br.com.ope.vo.GrupoFiltroVO

interface GrupoDAO {

    fun filtrar(grupoFiltroVO: GrupoFiltroVO) : List<Grupo>

}