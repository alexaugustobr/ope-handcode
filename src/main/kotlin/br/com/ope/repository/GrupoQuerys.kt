package br.com.ope.repository

class GrupoQuerys {

    companion object {
        val SELECT_GRUPO_JOIN_TURMA_CURSO =
                "select grupo.id grupo_id, " +
                "grupo.nome grupo_nome, " +
                "grupo.status grupo_status, " +
                "grupo.logo_hash grupo_logo_hash, " +
                "grupo.turma_id, " +
                "turma.letra turma_letra, " +
                "turma.ano turma_ano, " +
                "turma.semestre turma_semestre, " +
                "turma.curso_id, " +
                "curso.nome curso_nome, " +
                "curso.sigla curso_sigla " +
                "from core.grupo " +
                "inner join core.turma on turma.id = grupo.turma_id " +
                "inner join core.curso on curso.id = turma.curso_id " +
                "where "

    }

}