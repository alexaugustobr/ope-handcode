package br.com.ope

import br.com.ope.enumx.Role
import br.com.ope.model.*
import br.com.ope.repository.*
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
//@Profile(value = ["dev","default"])
class AppDevDatabaseRunner(val cursoRepository: CursoRepository,
                           val eventoRepository: EventoRepository,
                           val tarefaRepository: TarefaRepository,
                           val alunoRepository: AlunoRepository,
                           val usuarioRepository: UsuarioRepository,
                           val grupoRepository: GrupoRepository,
                           val disciplinaRepository: DisciplinaRepository,
                           val turmaRepository: TurmaRepository,
                           val entregaRepository: EntregaRepository) : ApplicationRunner{

    private val logger = LoggerFactory.getLogger(AppDevDatabaseRunner::class.java)
    override fun run(args: ApplicationArguments?) = iniciarBanco()

    fun iniciarBanco() {
        logger.info("Populando banco de com dados.")

        val ope1 = Disciplina(nome = "Oficina projeto empresa 1", sigla = "OPE1")
        disciplinaRepository.save(ope1)
        val ope2 = Disciplina(nome = "Oficina projeto empresa 2", sigla = "OPE2")
        disciplinaRepository.save(ope2)
        val ope3 = Disciplina(nome = "Oficina projeto empresa 3", sigla = "OPE3")
        disciplinaRepository.save(ope3)
        var ads = Curso("Analise de sistemas","ADS",4, disciplinaRepository.findAll())
        ads = cursoRepository.save(ads)

        var turmaA = Turma("B",4,2018,ads, mutableListOf(),Turma.Periodo.NOITE,ope2)
        turmaRepository.save(turmaA)
        var turmaB2 = Turma("A",4,2018,ads, mutableListOf(),Turma.Periodo.MANHA,ope2)
        turmaRepository.save(turmaB2)
        var turmaB = Turma("A",3,2018,ads, mutableListOf(),Turma.Periodo.NOITE,ope1)


        val admin = Administrador(nome = "Administrador", email = "admin@email.com.br", senha = BCryptPasswordEncoder().encode("senha"), ativo = true, permissoes = mutableSetOf(Role.ROLE_ADMIN))
        val yuri = Professor(nome = "Yuri", email = "professor@email.com.br", senha = BCryptPasswordEncoder().encode("senha"), ativo = true, permissoes = mutableSetOf(Role.ROLE_PROFESSOR))
        val fernando = Professor(nome = "Fernando", email = "fernando@email.com.br", senha = BCryptPasswordEncoder().encode("senha"), ativo = true, permissoes = mutableSetOf(Role.ROLE_PROFESSOR))



        usuarioRepository.save(admin)
        usuarioRepository.save(yuri)
        usuarioRepository.save(fernando)


        val handcode = Grupo(nome = "Handcode", curso = ads, alunos = mutableListOf(), turma = turmaA, tema = "Sistema gerenciador de OPE", disciplina = ope2 )

        grupoRepository.save(handcode)

        val alan = Aluno(grupo= handcode, nome = "Alan Faraj", ra = 1, senha = BCryptPasswordEncoder().encode("senha"), email = "alan@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)
        val alex = Aluno(grupo= handcode, nome = "Alex Augusto (Javaboy)", ra = 1700072, senha = BCryptPasswordEncoder().encode("senha"), email = "java@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)
        val michael = Aluno(grupo= handcode, nome = "Michael da Silva de Souza", ra = 1700381, senha = BCryptPasswordEncoder().encode("senha"), email = "michael@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)
        val cicinth = Aluno(grupo= handcode, nome = " Cinthia Queiroz", ra = 1700693, senha = BCryptPasswordEncoder().encode("senha"), email = "cicinth@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)
        val mestre = Aluno(grupo= handcode, nome = "Fabio Aurelio Abe Nogueira", ra = 1700603, senha = BCryptPasswordEncoder().encode("senha"), email = "mestre@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)
        val gabs = Aluno(grupo= handcode, nome = "Gabriel Bueno", ra = 1601606, senha = BCryptPasswordEncoder().encode("senha"), email = "gabs@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)
        val russo = Aluno(grupo= handcode, nome = "Henrique Borges da Silva", ra = 1700054, senha = BCryptPasswordEncoder().encode("senha"), email = "russo@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)
        val diego = Aluno(grupo= handcode, nome = "Diego santos", ra = 1700677, senha = BCryptPasswordEncoder().encode("senha"), email = "digao@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)

        usuarioRepository.save(alan)
        usuarioRepository.save(michael)
        usuarioRepository.save(alex)
        usuarioRepository.save(alan)
        usuarioRepository.save(cicinth)
        usuarioRepository.save(mestre)
        usuarioRepository.save(gabs)
        usuarioRepository.save(russo)
        usuarioRepository.save(diego)


        val aluno = Aluno(nome = "Aluno teste", ra = 123456, senha = BCryptPasswordEncoder().encode("impacta"), email = "aluno", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaA)
        usuarioRepository.save(aluno)

        val rodolfo = Aluno(nome = "Rodolfo", ra = 1700047, senha = BCryptPasswordEncoder().encode("senha"), email = "rodolfo@email.com.br", ativo = true, permissoes = mutableSetOf(Role.ROLE_ALUNO), turma = turmaB)
        usuarioRepository.save(rodolfo)

        val grupo = Grupo(nome = "Grupo do Rodolfo", curso = ads, alunos = mutableListOf(rodolfo, aluno), turma = turmaB, tema = "Tema do Rodolfo", disciplina = ope1)

        grupoRepository.save(grupo)

        //val tarefa = Tarefa(Date(), Date(), 1, mutableListOf(), "Lista de entregaveis.", "Entrega de parte da documentacao", ope1, ads, turmaRepository.findAllByCurso_idOrderBySemestreDesc(ads.id
        //        ?: UUID.randomUUID()))
//
        //tarefaRepository.save(tarefa)

       // val evento = Evento(Date(), "Lista de entregaveis.", "Entrega de parte da documentacao", ope1, ads, turmaRepository.findAllByCurso_idOrderBySemestreDesc(ads.id
       //         ?: UUID.randomUUID()), tarefa)

        //eventoRepository.save(evento)

        val atividades = mutableListOf<Tarefa>()

        val tarefa1 = Tarefa(Date(), "Lista de entregaveis", "Entrega de parte da documentacao 1", Arrays.asList(ope1), Arrays.asList(ads), Arrays.asList(turmaA), Arrays.asList())

        val tarefa2 = Tarefa(Date(), "Lista de entregaveis", "Entrega de parte da documentacao 1", Arrays.asList(ope1), Arrays.asList(ads), Arrays.asList(turmaA), Arrays.asList())

        atividades.add(tarefa1)
        atividades.add(tarefa2)

        tarefaRepository.saveAll(atividades)

        val entregas = mutableListOf<Entrega>()

        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.PENDENTE, tarefa1, handcode, mutableListOf()))

        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.PENDENTE, tarefa1, handcode, mutableListOf()))

        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.PENDENTE, tarefa1, handcode, mutableListOf()))

        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.PENDENTE, tarefa1, handcode, mutableListOf()))

        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.PENDENTE, tarefa1, handcode, mutableListOf()))



        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.REALIZADA, tarefa2, handcode, mutableListOf()))

        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.REALIZADA, tarefa2, handcode, mutableListOf()))

        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.REALIZADA, tarefa2, handcode, mutableListOf()))

        entregas.add(Entrega(handcode.disciplina!!, Date(), Entrega.Status.REALIZADA, tarefa2, handcode, mutableListOf()))

        entregaRepository.saveAll(entregas)

        logger.info("Finalizado setup dos dados simulados no banco")

        val eventos = mutableListOf<Evento>()

        val evento1 = Evento(Date(), "Lista de entregaveis", "Entrega de parte da documentacao 1", Arrays.asList(ope1), Arrays.asList(ads), Arrays.asList(turmaA))

        eventos.add(evento1)

        val evento2 = Evento(Date(), "Lista de entregaveis", "Entrega de parte da documentacao 1", Arrays.asList(ope1), Arrays.asList(ads), Arrays.asList(turmaA))

        eventos.add(evento2)

        eventoRepository.saveAll(eventos)
    }

}