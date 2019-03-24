package br.com.ope.service

import br.com.ope.model.Entrega
import br.com.ope.model.Tarefa
import br.com.ope.repository.DisciplinaRepository
import br.com.ope.repository.EntregaRepository
import br.com.ope.repository.GrupoRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class EntregaService(val entregaRepository: EntregaRepository,
                     val grupoRepository: GrupoRepository,
                     val disciplinaRepository: DisciplinaRepository) {

    fun gerarEntregasDaTarefa(tarefa: Tarefa) : MutableList<Entrega> {
        val disciplinas = disciplinaRepository.findAllByTarefas(tarefa)
        val disciplinasIdList = mutableListOf<UUID>()
        for (disciplina in disciplinas) {
            disciplinasIdList.add(disciplina.id!!)
        }
        val grupos = grupoRepository.findAllByTurma_Disciplina_IdIn(disciplinasIdList)

        val entregas : MutableList<Entrega> = mutableListOf()
        for (grupo in grupos) {
            entregas.add(Entrega(grupo = grupo, disciplina = grupo.turma!!.disciplina!!, tarefa = tarefa))
        }

        return entregas
    }

    fun salvar(entregas: MutableList<Entrega>) : MutableList<Entrega> {

        entregaRepository.saveAll(entregas)

        return entregas
    }

}