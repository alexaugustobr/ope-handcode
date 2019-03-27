package br.com.ope.vo

import br.com.ope.model.Curso
import br.com.ope.model.Grupo
import br.com.ope.model.Turma
import org.apache.commons.lang3.StringUtils
import java.util.*

data class GrupoFiltroVO(val periodos : List<Turma.Periodo?> = mutableListOf(),
                         val turmas : List<Turma?> = mutableListOf(),
                         val cursos : List<Curso?> = mutableListOf(),
                         val ano : Int = Calendar.getInstance().get(Calendar.YEAR),
                         val nomeGrupo : String? = "",
                         val status : List<Grupo.Status?> = mutableListOf(Grupo.Status.APROVADO, Grupo.Status.AGUARDANDO),
                         val paginaAtual : Int = 0
) {
    fun isFiltradoPorGrupoNome(): Boolean {
        return StringUtils.isNotBlank(nomeGrupo)
    }

    fun isFiltradoPorTurmas(): Boolean {
        return !turmas.isEmpty()
    }

    fun isFiltradoPorCursos(): Boolean {
        return !cursos.isEmpty()
    }

    fun isFiltradoPorStatus(): Boolean {
        return !status.isEmpty()
    }
}