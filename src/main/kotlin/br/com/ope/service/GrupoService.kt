package br.com.ope.service

import br.com.ope.dto.v1.GrupoCadastroV1DTO
import br.com.ope.exception.BusinessException
import br.com.ope.exception.EntidadeNaoEncontradaException
import br.com.ope.model.Grupo
import br.com.ope.repository.AlunoRepository
import br.com.ope.repository.GrupoRepository
import br.com.ope.validators.GrupoNovoValidator
import org.springframework.stereotype.Service
import org.springframework.validation.BindException
import java.util.*

@Service
class GrupoService(private val grupoRepository: GrupoRepository,
                   private val alunoRepository: AlunoRepository,
                   private val grupoNovoValidator: GrupoNovoValidator)  : DomainService() {

    @Throws(EntidadeNaoEncontradaException::class, BusinessException::class)
    fun removerAlunoDoGrupo(alunoId : UUID, grupoId : UUID, motivo: String = "") {
        val grupo = grupoRepository.findById(grupoId).orElseThrow { EntidadeNaoEncontradaException() }
        val aluno = alunoRepository.findById(alunoId).orElseThrow { EntidadeNaoEncontradaException() }
        grupo.removerAluno(aluno)
        grupoRepository.save(grupo)
        alunoRepository.save(aluno)
        //TODO ENVIAR EMAIL COM MOTIVO
    }

    @Throws(EntidadeNaoEncontradaException::class, BusinessException::class, BindException::class)
    fun cadastrarGrupo(grupo : Grupo) : Grupo {

        val errors = getBiddingErrorsForClass(grupo)

        grupoNovoValidator.validate( grupo, errors )

        if (errors.hasErrors()) throw BindException(errors)

        grupoRepository.save(grupo)

        for (aluno in grupo.alunos) {

            val alunoEncontrado = alunoRepository.findOneByRa(aluno.ra!!).orElse(aluno)

            alunoEncontrado.turma = grupo.turma
            alunoEncontrado.grupo = grupo
            alunoEncontrado.ativo = false
            alunoEncontrado.email = aluno.email
            alunoEncontrado.nome = aluno.nome
            alunoEncontrado.telefone = aluno.telefone

            alunoRepository.save(alunoEncontrado)
        }

        return grupo
    }



    @Throws(EntidadeNaoEncontradaException::class, BusinessException::class)
    fun cadastrarGrupo(grupoDTO : GrupoCadastroV1DTO) : GrupoCadastroV1DTO {
        val grupo = GrupoCadastroV1DTO.fromDomain(cadastrarGrupo(Grupo.criarNovoGrupo(grupoDTO)))
        return grupo
    }

}