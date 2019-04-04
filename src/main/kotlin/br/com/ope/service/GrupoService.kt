package br.com.ope.service

import br.com.ope.exception.BusinessException
import br.com.ope.exception.EntidadeNaoEncontradaException
import br.com.ope.repository.AlunoRepository
import br.com.ope.repository.GrupoRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GrupoService(private val grupoRepository: GrupoRepository, private val alunoRepository: AlunoRepository)  : DomainService() {

    @Throws(EntidadeNaoEncontradaException::class, BusinessException::class)
    fun removerAlunoDoGrupo(alunoId : UUID, grupoId : UUID, motivo: String? = "") {
        val grupo = grupoRepository.findById(grupoId).orElseThrow { EntidadeNaoEncontradaException() }
        val aluno = alunoRepository.findById(alunoId).orElseThrow { EntidadeNaoEncontradaException() }
        grupo.removerAluno(aluno)
        grupoRepository.save(grupo)
        alunoRepository.save(aluno)
        //TODO ENVIAR EMAIL COM MOTIVO
    }

}