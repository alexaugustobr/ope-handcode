package br.com.ope.api.home.v1

import br.com.ope.api.ApiRestController
import br.com.ope.dto.v1.GrupoCadastroV1DTO
import br.com.ope.service.GrupoService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/home/v1/grupos"], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
@Api(tags=["Home/V1/Grupos"], description = "Cadastro de grupos")
class GrupoCadastroApi(val grupoService: GrupoService) : ApiRestController() {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ApiOperation(
            value="Cadastra grupo com alunos",
            response= GrupoCadastroV1DTO::class,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            notes="Cadastra alunos no grupo, verifica pelo ra se o aluno já existe")
    @ApiResponses(value=[
        ApiResponse(
                code=200,
                message="Retorna grupo e alunos cadastrados com sucesso",
                response= GrupoCadastroV1DTO::class
        ),
        ApiResponse(
                code=400,
                message="Erro com causa mapeada"
        ),
        ApiResponse(
                code=500,
                message="Erro inesperado no servidor"
        )
    ]
    )
    @Throws(Exception::class)
    fun cadastrar(@RequestBody grupoCadastroV1DTO: GrupoCadastroV1DTO) : ResponseEntity<Any> {
        val grupo = grupoService.cadastrarGrupo(grupoCadastroV1DTO)
        return ResponseEntity.ok(grupo)
    }

}