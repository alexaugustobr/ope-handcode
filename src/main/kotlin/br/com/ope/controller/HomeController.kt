package br.com.ope.controller

import br.com.ope.model.Aluno
import br.com.ope.model.Grupo
import br.com.ope.model.Turma
import br.com.ope.repository.AlunoRepository
import br.com.ope.repository.CursoRepository
import br.com.ope.repository.GrupoRepository
import br.com.ope.service.GrupoService
import br.com.ope.validators.GrupoNovoValidator
import br.com.ope.vo.MensagemVO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
class HomeController {

    val cursoRepository : CursoRepository
    val grupoRepository : GrupoRepository
    val alunoRepository : AlunoRepository
    val grupoNovoValidator : GrupoNovoValidator
    val grupoService : GrupoService

    constructor(cursoRepository: CursoRepository, grupoRepository: GrupoRepository, alunoRepository: AlunoRepository, grupoNovoValidator: GrupoNovoValidator, grupoService: GrupoService) {
        this.cursoRepository = cursoRepository
        this.grupoRepository = grupoRepository
        this.alunoRepository = alunoRepository
        this.grupoNovoValidator = grupoNovoValidator
        this.grupoService = grupoService
    }

    @GetMapping("/")
    fun index(model : Model) : String {

        return "redirect:/grupos"
    }

    @GetMapping("/grupos")
    fun grupos(model : Model, grupo: Grupo) : String {

        if (grupo.alunos.isEmpty()) {
            grupo.alunos.add(Aluno(UUID.randomUUID()))
        }

        val turmas = mutableListOf<Turma>()

        if (grupo.turma != null) {
            turmas.addAll(grupo.turma!!.curso!!.turmas)
        }

        model.addAttribute("grupo", grupo)
        model.addAttribute("cursos", cursoRepository.findAllByOrderByNome())
        model.addAttribute("turmas", turmas)

        return "home/grupos"
    }

    @PostMapping("/grupos")
    fun gruposSalvar(model : Model, redirectAttributes: RedirectAttributes, grupo: Grupo, bindingResult : BindingResult) : String {

        try {
            grupoService.cadastrarGrupo(grupo)
            redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Aguarde a aprovação do administrador por email, para poder acessar a plataforma!","Grupo salvo!", MensagemVO.TipoMensagem.success ))
        } catch (e : BindException) {
            e.printStackTrace()
            for (allError in e.allErrors) {
                bindingResult.addError(allError)
            }
            return this.grupos(model,grupo)
        } catch (e : Exception) {
            e.printStackTrace()
            redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Ocorreu um erro interno.","Erro!", MensagemVO.TipoMensagem.danger ))
        }

        return "redirect:/grupos"
    }

    @PostMapping("/grupos/adicionar-aluno")
    fun grupoAdicionarAluno(model : Model, redirectAttributes: RedirectAttributes, grupo: Grupo, bindingResult: BindingResult) : String {

        grupo.alunos.add(Aluno(UUID.randomUUID()))

        return this.grupos(model, grupo)
    }

    @PostMapping("/grupos/remover-aluno/{id}")
    fun grupoRemoverAluno(model : Model, redirectAttributes: RedirectAttributes, grupo: Grupo, bindingResult: BindingResult, @PathVariable id : UUID) : String {

        var alunoRemover : Aluno? = null
        for (aluno in grupo.alunos) {
            if (aluno.id == id) {
                alunoRemover = aluno
                break
            }
        }

        if (alunoRemover!= null) {
            grupo.alunos.remove(alunoRemover)
        }

        return this.grupos(model, grupo)
    }

}