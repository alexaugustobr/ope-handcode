package br.com.ope.controller

import br.com.ope.model.Aluno
import br.com.ope.model.Entrega
import br.com.ope.model.Grupo
import br.com.ope.repository.AlunoRepository
import br.com.ope.repository.EntregaRepository
import br.com.ope.repository.GrupoRepository
import br.com.ope.repository.TarefaRepository
import br.com.ope.vo.MensagemVO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/painel/admin/grupos")
class PainelAdminGruposController {

    val grupoRepository : GrupoRepository
    val tarefaRepository : TarefaRepository
    val entregaRepository : EntregaRepository
    val alunoRepository : AlunoRepository

    constructor(grupoRepository: GrupoRepository, tarefaRepository: TarefaRepository, entregaRepository: EntregaRepository, alunoRepository: AlunoRepository) {
        this.grupoRepository = grupoRepository
        this.tarefaRepository = tarefaRepository
        this.entregaRepository = entregaRepository
        this.alunoRepository = alunoRepository
    }

    @GetMapping
    fun index(model : Model) : String {
        model.addAttribute("grupos", grupoRepository.findAll())
        return "painel/admin/grupos/index"
    }

    @GetMapping("/{id}")
    fun detalhe(model : Model, @PathVariable id : UUID, redirectAttributes: RedirectAttributes) : String {

        val grupo = grupoRepository.findById(id) //TODO JOIN DISCIPLINA

        if (!grupo.isPresent) return redirectGrupoNaoEncontrado(model, redirectAttributes)

        val entregasPendentes  = entregaRepository.findAllByStatusAndGrupoOrderByDataCriacaoDesc(Entrega.Status.PENDENTE , grupo.get())
        val entregasRealizadas = entregaRepository.findAllByStatusAndGrupoOrderByDataCriacaoDesc(Entrega.Status.REALIZADA , grupo.get())

        model.addAttribute("grupo", grupo.get())
        model.addAttribute("entregasPendentes", entregasPendentes)
        model.addAttribute("entregasRealizadas", entregasRealizadas)
        return "painel/admin/grupos/detalhe"
    }

    private fun redirectGrupoNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Grupo não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/admin/grupos"
    }


    @GetMapping("/{id}/aprovar")
    fun aprovar(model : Model, @PathVariable id : UUID, redirectAttributes: RedirectAttributes) : String {

        val grupo = grupoRepository.findById(id) //TODO JOIN DISCIPLINA

        if (!grupo.isPresent) return redirectGrupoNaoEncontrado(model, redirectAttributes)

        grupo.get().status = Grupo.Status.APROVADO

        grupoRepository.save(grupo.get())

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Grupo aprovado!","Sucesso!", MensagemVO.TipoMensagem.success ))

        return "redirect:/painel/admin/grupos/$id"
    }


    @GetMapping("/{id}/recusar")
    fun recusar(model : Model, @PathVariable id : UUID, redirectAttributes: RedirectAttributes, @RequestParam(required = false) motivo : String?) : String {

        val grupo = grupoRepository.findById(id) //TODO JOIN DISCIPLINA

        if (!grupo.isPresent) return redirectGrupoNaoEncontrado(model, redirectAttributes)

        grupo.get().status = Grupo.Status.RECUSADO

        val alunosRemovidos = mutableListOf<Aluno>()
        for (aluno in grupo.get().alunos) {
            aluno.grupo = null
            alunosRemovidos.add(aluno)
        }
        grupo.get().alunos = mutableListOf()
        alunoRepository.saveAll(alunosRemovidos)

        grupo.get().alunosRemovidos = alunosRemovidos

        grupoRepository.save(grupo.get())

        //TODO ENVIAR EMAIL COM MOTIVO

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Grupo recusado!","Sucesso!", MensagemVO.TipoMensagem.warning ))

        return "redirect:/painel/admin/grupos/$id"
    }

    @GetMapping("/{id}/remover-integrante/{alunoId}")
    fun removerIntegrante(model : Model, @PathVariable id : UUID, @PathVariable alunoId : UUID, redirectAttributes: RedirectAttributes, @RequestParam(required = false) motivo : String?) : String {

        val aluno = alunoRepository.findById(alunoId)

        if (!aluno.isPresent){
            redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Aluno nao encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
            return "redirect:/painel/admin/grupos/$id"
        }

        val grupo = aluno.get().grupo!!
        aluno.get().grupo = null
        aluno.get().gruposRemovidos.add(grupo)
        alunoRepository.save(aluno.get())
        grupo.alunosRemovidos.add(aluno.get())
        grupoRepository.save(grupo)

        //TODO ENVIAR EMAIL COM MOTIVO

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Aluno removido!","Sucesso!", MensagemVO.TipoMensagem.warning ))

        return "redirect:/painel/admin/grupos/$id"
    }

}