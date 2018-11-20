package br.com.ope.controller

import br.com.ope.model.*
import br.com.ope.repository.*
import br.com.ope.service.FileStorage
import br.com.ope.service.TarefaService
import br.com.ope.vo.MensagemVO
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/painel/professor/tarefas")
class PainelProfessorTarefasController {

    val grupoRepository: GrupoRepository
    val professorRepository: ProfessorRepository
    val entregaRepository: EntregaRepository
    val turmaRepository: TurmaRepository
    val cursoRepository: CursoRepository
    val disciplinaRepository: DisciplinaRepository
    val tarefaRepository: TarefaRepository
    val tarefaService: TarefaService
    val arquivoRepository: ArquivoRepository
    val fileStorage: FileStorage

    constructor(grupoRepository: GrupoRepository, professorRepository: ProfessorRepository, entregaRepository: EntregaRepository, turmaRepository: TurmaRepository, cursoRepository: CursoRepository, disciplinaRepository: DisciplinaRepository, tarefaRepository: TarefaRepository, tarefaService: TarefaService, arquivoRepository: ArquivoRepository, fileStorage: FileStorage) {
        this.grupoRepository = grupoRepository
        this.professorRepository = professorRepository
        this.entregaRepository = entregaRepository
        this.turmaRepository = turmaRepository
        this.cursoRepository = cursoRepository
        this.disciplinaRepository = disciplinaRepository
        this.tarefaRepository = tarefaRepository
        this.tarefaService = tarefaService
        this.arquivoRepository = arquivoRepository
        this.fileStorage = fileStorage
    }


    @GetMapping()
    fun index(model : Model, @AuthenticationPrincipal usuario: Usuario) : String {

        val entregasPendentes  = entregaRepository.findAllByStatusAndProfessorAvaliador_idAndNotaIsNullOrderByTarefa_DataHoraDesc(Entrega.Status.REALIZADA , usuario.id)
        val entregasCorrigidas = entregaRepository.findAllByStatusAndProfessorAvaliador_idAndNotaIsNotNullOrderByTarefa_DataHoraDesc(Entrega.Status.REALIZADA , usuario.id)

        val tarefaPendenteMap : MultiValueMap<Tarefa, Entrega> = LinkedMultiValueMap()
        val tarefaCorrigidaMap : MultiValueMap<Tarefa, Entrega> = LinkedMultiValueMap()

        for (entrega in entregasPendentes) {
            tarefaPendenteMap.add(entrega.tarefa!!, entrega)
        }

        for (entrega in entregasCorrigidas) {
            tarefaCorrigidaMap.add(entrega.tarefa!!, entrega)
        }

        model.addAttribute("tarefaPendenteMap", tarefaPendenteMap)
        model.addAttribute("tarefaCorrigidaMap", tarefaCorrigidaMap)
        return "painel/professor/tarefas/index"
    }

    @PostMapping("/entregas/{entregaId}/corrigir")
    fun corrigir (model: Model, @AuthenticationPrincipal usuario: Usuario, @PathVariable entregaId: UUID, entregaCorrigida: Entrega, anexos: List<MultipartFile>, redirectAttributes: RedirectAttributes) : String {

        val entregaOptional = entregaRepository.findById(entregaId)

        if (!entregaOptional.isPresent) return this.redirectTarefaNaoEncontrado(model, redirectAttributes)

        val entrega = entregaOptional.get()

        entrega.comentario = entregaCorrigida.comentario
        entrega.professorAvaliador = usuario as Professor
        entrega.nota = entregaCorrigida.nota
        for (arquivo in anexos) {
            val arquivoReferencia = Arquivo.Builder().of(arquivo).build()
            arquivoRepository.save(arquivoReferencia)
            fileStorage.store(arquivo, arquivoReferencia.id!!)
            entrega.arquivosCorrecao.add(arquivoReferencia)
        }

        entregaRepository.save(entrega)

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa corrigida!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/professor/tarefas"
    }

    private fun redirectTarefaNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/professor/tarefas"
    }

}