package br.com.ope.validators

import br.com.ope.model.Grupo
import br.com.ope.repository.AlunoRepository
import br.com.ope.repository.GrupoRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class GrupoNovoValidator : Validator {

    val alunoRepository: AlunoRepository
    val grupoRepository: GrupoRepository
    val alunoNovoValidator: AlunoNovoValidator

    constructor(alunoRepository: AlunoRepository, grupoRepository: GrupoRepository, alunoNovoValidator: AlunoNovoValidator) {
        this.alunoRepository = alunoRepository
        this.grupoRepository = grupoRepository
        this.alunoNovoValidator = alunoNovoValidator
    }

    override fun validate(target: Any?, errors: Errors) {

        val grupo = target!! as Grupo

        ValidatorUtil.validate(grupo, errors)

        this.validarAlunos(grupo, errors)

    }

    private fun validarAlunos(grupoValidado: Grupo, errors: Errors) {

        var i = 0

        val emailList = mutableListOf<String>()
        val raList = mutableListOf<Long>()

        for (alunoValidado in grupoValidado.alunos) {
            errors.pushNestedPath(String.format("alunos[%d]", i))

            if (alunoValidado.ra != null) {
                val ra = alunoValidado.ra!!

                val alunoEncontrado = alunoRepository.findOneByRa(ra) //TODO JOIN GRUPO
                if (alunoEncontrado.isPresent && alunoEncontrado.get().grupo != null) {
                    errors.rejectValue("ra", "exists", "Aluno do está ativo em outro grupo.")
                }

                if (raList.contains(ra)) {
                    errors.rejectValue("ra", "exists", "Já existe uma aluno com o esse RA.")
                }

                raList.add(ra)
            }

            val email = alunoValidado.email
            if (!StringUtils.isBlank(email)) {

                val alunoEncontrado = alunoRepository.findOneByEmail(email) //TODO JOIN GRUPO
                if (alunoEncontrado.isPresent && alunoEncontrado.get().grupo != null) {
                    errors.rejectValue("email", "exists", "Já existe um aluno utilizando este email.")
                }

                if (emailList.contains(email)) {
                    errors.rejectValue("email", "exists", "Já existe uma aluno com o esse email.")
                }

                emailList.add(email)
            }

            errors.popNestedPath()

            i++

        }


    }

    override fun supports(clazz: Class<*>) = Grupo::class.java.isAssignableFrom(clazz)

    companion object {
        const val REQUIRED = "required"
    }
}