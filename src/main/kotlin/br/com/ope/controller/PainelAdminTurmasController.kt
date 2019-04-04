package br.com.ope.controller

import br.com.ope.model.Turma
import br.com.ope.repository.CursoRepository
import br.com.ope.repository.DisciplinaRepository
import br.com.ope.repository.TurmaRepository
import br.com.ope.vo.MensagemVO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*
import javax.validation.Valid

@Controller
@RequestMapping("/painel/admin/turmas")
class PainelAdminTurmasController {
    val cursoRepository : CursoRepository
    val turmaRepository : TurmaRepository
    val disciplinaRepository : DisciplinaRepository

    constructor(cursoRepository: CursoRepository, turmaRepository: TurmaRepository, disciplinaRepository: DisciplinaRepository) {
        this.cursoRepository = cursoRepository
        this.turmaRepository = turmaRepository
        this.disciplinaRepository = disciplinaRepository
    }

    @GetMapping
    fun index(model : Model) : String {
        model.addAttribute("turmas", turmaRepository.findAll())
        return "painel/admin/turmas/index"
    }

    @GetMapping("/novo")
    fun novo(model: Model, turma : Turma) : String {
        model.addAttribute("turma", turma)
        model.addAttribute("periodos", Turma.Periodo.values())
        model.addAttribute("cursos", cursoRepository.findAllByExcluidoIsFalse())
        model.addAttribute("disciplinas", disciplinaRepository.findAllByDataExclusaoIsNull())
        return "painel/admin/turmas/novo"
    }

    @PostMapping("/novo")
    fun novoSalvar(model: Model, @Valid turma : Turma, bindingResult: BindingResult) : String {

        if (bindingResult.hasErrors()) return this.novo(model, turma)

       turmaRepository.save(turma)
       return "redirect:/painel/admin/turmas"
    }
    @GetMapping("/{id}")
    fun editar(redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID) : String {
        var turma = turmaRepository.findById(id)

        if(!turma.isPresent){
            redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Turma nao encontrada", "Erro", MensagemVO.TipoMensagem.danger))
            return "redirect:/painel/admin/turmas"
        }
        model.addAttribute("turma", turma.get())
        model.addAttribute("periodos", Turma.Periodo.values())
        model.addAttribute("cursos", cursoRepository.findAllByExcluidoIsFalse())
        model.addAttribute("disciplinas", disciplinaRepository.findAllByDataExclusaoIsNull())
        return "painel/admin/turmas/editar"
    }
    @PostMapping("/{id}")
    fun editarSalvar(redirectAttributes: RedirectAttributes , model : Model, @PathVariable id : UUID, @Valid turma : Turma, bindingResult: BindingResult) : String {
        if (bindingResult.hasErrors()){
            model.addAttribute("periodos", Turma.Periodo.values())
            model.addAttribute("cursos", cursoRepository.findAllByExcluidoIsFalse())
            return "painel/admin/turmas/editar"
        }
        var turmaBanco = turmaRepository.findById(id)

        if(!turmaBanco.isPresent){
            redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Turma nao encontrada", "Erro", MensagemVO.TipoMensagem.danger))
            return "redirect:/painel/admin/turmas"
        }

        turmaRepository.save(turmaBanco.get().atualizar(turma))
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Turma atualizada", "Sucesso!", MensagemVO.TipoMensagem.success))

        return "redirect:/painel/admin/turmas"
    }
}