package br.com.ope.controller.rest

import br.com.ope.api.ApiRestController
import br.com.ope.dto.v1.TurmaV1DTO
import br.com.ope.repository.TurmaRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(value = ["/api/home/v1/turmas"])
@Api(tags=["Home/V1/Turmas"], description = "Listagem de turmas por curso")
class TurmaApiHomeV1 : ApiRestController {

    private val turmaRepository : TurmaRepository

    constructor(turmaRepository: TurmaRepository) : super() {
        this.turmaRepository = turmaRepository
    }

    @ApiOperation(
            value="Listar as turmas por cursos",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response=TurmaV1DTO::class,
            notes="Essa operação necessita de um ou mais parametros de cursoId")
    @ApiResponses(value=[
            ApiResponse(
                    code=200,
                    message="Retorna a Lista de Turmas com uma mensagem de sucesso",
                    response=TurmaV1DTO::class
            ),
            ApiResponse(
                code=204,
                message="Retorna resposta vazia"
            ),
            ApiResponse(
                    code=400,
                    message="Erro mapeado no servidor"
            ),
            ApiResponse(
                    code=500,
                    message="Erro inesperado no servidor"
            )
        ]
    )
    @GetMapping
    fun turmas (@RequestParam("cursoId") cursoId : List<UUID>) :  ResponseEntity<Any> {

        val turmas = turmaRepository.findAllByCurso_idInAndExcluidoIsFalseOrderBySemestreDesc(cursoId)
        if (turmas.isEmpty()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(turmas)
    }

}