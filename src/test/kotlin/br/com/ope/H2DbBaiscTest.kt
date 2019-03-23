package br.com.ope

import br.com.ope.model.*
import br.com.ope.repository.CursoRepository
import br.com.ope.repository.DisciplinaRepository
import br.com.ope.repository.GrupoRepository
import br.com.ope.repository.TurmaRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*
import javax.transaction.Transactional

@RunWith(SpringJUnit4ClassRunner::class)
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
//@PropertySource("src/test/resources/application-pgtest.properties")
class H2DbBaiscTest {

    @Autowired
    lateinit var disciplinaRepository: DisciplinaRepository
    @Autowired
    lateinit var grupoRepository: GrupoRepository
    @Autowired
    lateinit var turmaRepository: TurmaRepository
    @Autowired
    lateinit var cursoRepository: CursoRepository

    @Test
    fun cadastrar_duas_disciplinas_e_bucar_as_duas_de_uma_vez() {

        val disciplinaA = disciplinaRepository.save(Disciplina(nome = "OPE1", sigla = "OPE1"))
        val disciplinaB = disciplinaRepository.save(Disciplina(nome = "OPE2", sigla = "OPE2"))
        val cursoAds = cursoRepository.save(Curso(nome = "nome", sigla = "A", disciplinas = mutableListOf<Disciplina>(), semestres = 1))

        val turmaA = Turma(letra = "A",semestre = 1,ano = 2019, alunos = mutableListOf<Aluno>(), disciplina = disciplinaA, curso = cursoAds, periodo = Turma.Periodo.NOITE)

        turmaRepository.save(turmaA)

        Assert.assertNotNull(disciplinaA.id)
        Assert.assertNotNull(disciplinaB.id)

        val disciplinaList = disciplinaRepository.findAllById(Arrays.asList(disciplinaA.id, disciplinaB.id))

        Assert.assertFalse(disciplinaList.isEmpty())

        grupoRepository.save(Grupo(nome = "grupo1",disciplina = disciplinaA, turma = turmaA, curso = cursoAds,alunos = mutableListOf<Aluno>(), alunosRemovidos = mutableListOf<Aluno>(), id = null, tema = "tema"))
        grupoRepository.save(Grupo(nome = "grupo2",disciplina = disciplinaA, turma = turmaA, curso = cursoAds,alunos = mutableListOf<Aluno>(), alunosRemovidos = mutableListOf<Aluno>(), id = null, tema = "tema"))

        Assert.assertFalse(grupoRepository.findAllByDisciplina_IdIn(Arrays.asList(disciplinaA.id, disciplinaB.id)).isEmpty())


    }


}