package br.com.ope

import br.com.ope.enumx.Role
import br.com.ope.model.*
import br.com.ope.repository.*
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

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
                           val arquivoRepository: ArquivoRepository,
                           val entregaRepository: EntregaRepository) : ApplicationRunner{

    private val logger = LoggerFactory.getLogger(AppDevDatabaseRunner::class.java)
    override fun run(args: ApplicationArguments?) = iniciarBanco()

    fun iniciarBanco() {
        logger.info("Populando banco de com dados.")

        if (!usuarioRepository.findAll().isEmpty()) {
            logger.info("Base já esta populada.")
            return
        }

        val admin = Administrador(nome = "Administrador", email = "admin@email.com.br", senha = BCryptPasswordEncoder().encode("senha"), ativo = true, permissoes = mutableSetOf(Role.ROLE_ADMIN))
        val yuri = Professor(nome = "Yuri", email = "yuri@email.com.br", senha = BCryptPasswordEncoder().encode("senha"), ativo = true, permissoes = mutableSetOf(Role.ROLE_PROFESSOR))
        val professor = Professor(nome = "Professor Teste", email = "professor@email.com.br", senha = BCryptPasswordEncoder().encode("senha"), ativo = true, permissoes = mutableSetOf(Role.ROLE_PROFESSOR))

        usuarioRepository.save(admin)
        usuarioRepository.save(yuri)
        usuarioRepository.save(professor)

        val ope1 = Disciplina(nome = "Oficina projeto empresa 1", sigla = "OPE1")
        disciplinaRepository.save(ope1)
        val ope2 = Disciplina(nome = "Oficina projeto empresa 2", sigla = "OPE2")
        disciplinaRepository.save(ope2)
        val ope3 = Disciplina(nome = "Oficina projeto empresa 3", sigla = "OPE3")
        disciplinaRepository.save(ope3)
        var ads = Curso("Analise de sistemas","ADS",4, disciplinaRepository.findAll())
        ads = cursoRepository.save(ads)
        val ope4 = Disciplina(nome = "Oficina projeto empresa 4", sigla = "OPE4")
        disciplinaRepository.save(ope4)
        var si = Curso("Sistemas da Informação","SI",6, disciplinaRepository.findAll())
        si = cursoRepository.save(si)

        var turmaA = Turma("A",2,2019,ads, mutableListOf(),Turma.Periodo.NOITE,ope1)
        turmaRepository.save(turmaA)
        var turmaB = Turma("B",3,2019,ads, mutableListOf(),Turma.Periodo.MANHA,ope2)
        turmaRepository.save(turmaB)
        var turmaASI = Turma("A",4,2019,si, mutableListOf(),Turma.Periodo.NOITE,ope3)
        turmaRepository.save(turmaASI)


        val qtdAlunoPorGrupo = 4
        val qtdGrupo = 100

        val senhaPadrao = BCryptPasswordEncoder().encode("senha")

        val grupoList = mutableListOf<Grupo>()
        val alunoList = mutableListOf<Aluno>()

        var alunoIndex = 1

        for(a in 1..qtdGrupo) {

            val grupo : Grupo
            val turma : Turma

            if (a <= qtdGrupo/2) {
                grupo = Grupo(nome = "Grupo$a", curso = ads, alunos = mutableListOf(), turma = turmaA, tema = "Sistema gerenciador de OPE" )
                turma = turmaA
            } else {
                grupo = Grupo(nome = "Grupo$a", curso = si, alunos = mutableListOf(), turma = turmaASI, tema = "Sistema gerenciador de OPE" )
                turma = turmaASI
            }

            grupoList.add(grupo)

            for(b in 1..qtdAlunoPorGrupo) {
                val aluno = Aluno("Aluno$alunoIndex", "aluno$alunoIndex@email.com.br",false,senhaPadrao,mutableSetOf(Role.ROLE_ALUNO), (170000+alunoIndex).toLong(),grupo, mutableListOf<Grupo>(),turma)
                alunoList.add(aluno)
                alunoIndex++
            }

        }
        grupoRepository.saveAll(grupoList)
        alunoRepository.saveAll(alunoList)



        logger.info("Finalizado setup dos dados simulados no banco")

        logger.info("Servidor iniciado.")
    }

}