package br.com.ope.repository

import br.com.ope.model.Curso
import br.com.ope.model.Grupo
import br.com.ope.model.Turma
import br.com.ope.vo.GrupoFiltroVO
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*


@Repository
class GrupoDAOImpl(val jdbcTemplate: JdbcTemplate) : GrupoDAO {

    override fun filtrar(grupoFiltroVO: GrupoFiltroVO): List<Grupo> {
        val voList = mutableListOf<Grupo>()

        var conn: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            conn = jdbcTemplate.dataSource!!.connection
            val sql = aplicarFiltrosPaginacaoNaQuery(GrupoQuerys.SELECT_GRUPO_JOIN_TURMA_CURSO, grupoFiltroVO)
            ps = conn.prepareStatement(sql)
            aplicarParametrosDoFiltroEmPreparedStatement(ps, grupoFiltroVO, conn)
            rs = ps.executeQuery()

            while (rs.next()) {

                val curso = Curso(
                        id = rs.getObject("curso_id") as UUID,
                        nome = rs.getString("curso_nome"),
                        sigla = rs.getString("curso_sigla")
                )
                val turma = Turma(
                        id = rs.getObject("turma_id") as UUID,
                        letra = rs.getString("turma_letra"),
                        ano = rs.getInt("turma_ano"),
                        semestre = rs.getInt("turma_semestre"),
                        curso = curso
                )
                val grupo = Grupo(
                        turma = turma,
                        id = rs.getObject("grupo_id") as UUID,
                        nome = rs.getString("grupo_nome"),
                        status = Grupo.Status.valueOf(rs.getString("grupo_status")),
                        logoHash = rs.getObject("grupo_logo_hash") as UUID?
                )

                voList.add(grupo)

            }
            rs.close()
            ps.close()
            conn.close()
        } catch (e : Exception) {
            throw e
        } finally {
            try {
                rs?.close()
            } catch (e : Exception) {

            }
            try {
                ps?.close()
            } catch (e : Exception) {

            }
            try {
                conn?.close()
            } catch (e : Exception) {

            }
        }
        return voList
    }

    private fun aplicarFiltrosPaginacaoNaQuery(sqlBase: String, grupoFiltroVO: GrupoFiltroVO): String {

        //Futura implementacao de pagina
        //"select count(*) over() as total_registros, (count(*) over())/15 as total_paginas, * from ( " +
        // ") as subquery "// +
        //LIMIT 15 OFFSET (6) * 15

        val builder = StringBuilder(sqlBase)

        builder.append("turma.ano = ?")

        if (grupoFiltroVO.isFiltradoPorGrupoNome()) {
            builder.append(" and grupo.nome ilike concat('%',?,'%') ")
        }
        if (grupoFiltroVO.isFiltradoPorTurmas()) {
            builder.append(" and turma.id in ( ")
            val iterator = grupoFiltroVO.turmas.iterator()
            while (iterator.hasNext()) {
                iterator.next()
                builder.append(" ? ")
                if (iterator.hasNext()) {
                    builder.append(" , ")
                }
            }
            builder.append(" ) ")
        }
        if (grupoFiltroVO.isFiltradoPorCursos()) {
            builder.append( "and curso.id in ( ")
            val iterator = grupoFiltroVO.cursos.iterator()
            while (iterator.hasNext()) {
                iterator.next()
                builder.append(" ? ")
                if (iterator.hasNext()) {
                    builder.append(" , ")
                }
            }
            builder.append(" ) ")
        }
        if (grupoFiltroVO.isFiltradoPorStatus()) {
            builder.append( "and grupo.status in ( ")
            val iterator = grupoFiltroVO.status.iterator()
            while (iterator.hasNext()) {
                iterator.next()
                builder.append(" ? ")
                if (iterator.hasNext()) {
                    builder.append(" , ")
                }
            }
            builder.append(" ) ")
        }

        builder.append(" ORDER by grupo.nome ")

        return builder.toString()

    }

    private fun aplicarParametrosDoFiltroEmPreparedStatement(ps: PreparedStatement, grupoFiltroVO: GrupoFiltroVO, conn: Connection) {

        var posicao : Int = 1

        ps.setInt(posicao, grupoFiltroVO.ano)

        posicao++

        if (grupoFiltroVO.isFiltradoPorGrupoNome()) {
            ps.setString(posicao, grupoFiltroVO.nomeGrupo)
            posicao++
        }
        if (grupoFiltroVO.isFiltradoPorTurmas()) {
            for (turma in grupoFiltroVO.turmas) {
                ps.setObject(posicao, turma?.id)
                posicao++
            }
        }
        if (grupoFiltroVO.isFiltradoPorCursos()) {
            for (curso in grupoFiltroVO.cursos) {
                ps.setObject(posicao, curso?.id)
                posicao++
            }
        }
        if (grupoFiltroVO.isFiltradoPorStatus()) {
            for (statuses in grupoFiltroVO.status) {
                ps.setString(posicao, statuses.toString())
                posicao++
            }
        }



    }

}