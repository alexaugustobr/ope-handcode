package br.com.ope.controller

import br.com.ope.model.Arquivo
import br.com.ope.model.Entrega
import br.com.ope.repository.*
import br.com.ope.service.FileStorage
import br.com.ope.vo.MensagemVO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@Controller
@RequestMapping("/painel/aluno/entregas")
class PainelAlunoEntregasController {

    val grupoRepository: GrupoRepository
    val alunoRepository: AlunoRepository
    val entregaRepository: EntregaRepository
    val turmaRepository: TurmaRepository
    val cursoRepository: CursoRepository
    val disciplinaRepository: DisciplinaRepository
    val arquivoRepository: ArquivoRepository
    val fileStorage : FileStorage
    val professorRepository: ProfessorRepository

    constructor(grupoRepository: GrupoRepository, alunoRepository: AlunoRepository, entregaRepository: EntregaRepository, turmaRepository: TurmaRepository, cursoRepository: CursoRepository, disciplinaRepository: DisciplinaRepository, arquivoRepository: ArquivoRepository, fileStorage: FileStorage, professorRepository: ProfessorRepository) {
        this.grupoRepository = grupoRepository
        this.alunoRepository = alunoRepository
        this.entregaRepository = entregaRepository
        this.turmaRepository = turmaRepository
        this.cursoRepository = cursoRepository
        this.disciplinaRepository = disciplinaRepository
        this.arquivoRepository = arquivoRepository
        this.fileStorage = fileStorage
        this.professorRepository = professorRepository
    }

    @PostMapping("/{id}/enviar")
    fun enviar(redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID, arquivos: List<MultipartFile>) : String {
        val entrega = entregaRepository.findById(id)

        if (!entrega.isPresent) return redirectEntregaNaoEncontrado(model, redirectAttributes)

        val dataAtual = LocalDate.now()

        val dataEntregaLimite = entrega.get().tarefa!!.dataHora!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if (dataAtual.isAfter(dataEntregaLimite)) return redirectEntregaPassouData(model, redirectAttributes)

        for (arquivo in arquivos) {
            val arquivoReferencia = Arquivo.Builder().of(arquivo).build()
            arquivoRepository.save(arquivoReferencia)
            fileStorage.store(arquivo, arquivoReferencia.id!!)
            entrega.get().arquivos.add(arquivoReferencia)
        }

        entregaRepository.save(entrega.get())

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Arquivo enviado!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/aluno/tarefas"
    }

    @PostMapping("/{id}/marcar-como-entregue")
    fun entregar(redirectAttributes: RedirectAttributes, model : Model, @PathVariable id : UUID) : String {
        val entrega = entregaRepository.findById(id)

        if (!entrega.isPresent) return redirectEntregaNaoEncontrado(model, redirectAttributes)

        val dataAtual = LocalDate.now()

        val dataEntregaLimite = entrega.get().tarefa!!.dataHora!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if (dataAtual.isAfter(dataEntregaLimite)) return redirectEntregaPassouData(model, redirectAttributes)

        if (entrega.get().status.equals(Entrega.Status.REALIZADA) ) return redirectEntregaJaRealizada(model, redirectAttributes)

        entrega.get().dataEnvio = Date()
        entrega.get().status = Entrega.Status.REALIZADA

        val professores = professorRepository.findAll()

        if (professores.isEmpty()) throw RuntimeException("Nao foi possivel adicionar um professor avaliador, nenhum professor cadastrado.")

        entrega.get().professorAvaliador = professores.get(ThreadLocalRandom.current().nextInt(0, professores.size))

        entregaRepository.save(entrega.get())

        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Marcado como entregue!","Sucesso!", MensagemVO.TipoMensagem.success ))
        return "redirect:/painel/aluno/tarefas"
    }

    private fun redirectEntregaNaoEncontrado(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Entrega não encontrado!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/aluno/tarefas"
    }

    private fun redirectEntregaPassouData(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa não pode ser entregue, pois esta além data de limite da entrega!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/aluno/tarefas"
    }

    private fun redirectEntregaJaRealizada(model: Model, redirectAttributes: RedirectAttributes): String {
        redirectAttributes.addFlashAttribute("mensagem", MensagemVO("Tarefa já está entregue!","Erro!", MensagemVO.TipoMensagem.danger ))
        return "redirect:/painel/aluno/tarefas"
    }

}