package br.com.ope.controller.rest

import br.com.ope.api.ApiRestController
import br.com.ope.dto.v1.CursoV1DTO
import br.com.ope.repository.CursoRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/home/v1/cursos"])
@Api(tags=["Home/V1/Cursos"], description = "Listagem de cursos")
class CursoApiHomeV1 : ApiRestController  {

    private val cursoRepository : CursoRepository

    constructor(cursoRepository: CursoRepository) : super() {
        this.cursoRepository = cursoRepository
    }

    @ApiOperation(
            value="Listar os cursos",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response= CursoV1DTO::class
    )
    @ApiResponses(value=[
        ApiResponse(
                code=200,
                message="Retorna a Lista de cursos com uma mensagem de sucesso",
                response= CursoV1DTO::class
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
    fun cursos () :  ResponseEntity<Any> {
        val cursos = cursoRepository.findAllByExcluidoIsFalse()
        if (cursos.isEmpty()) return ResponseEntity.noContent().build()
        return ResponseEntity.ok(cursos)
    }

}