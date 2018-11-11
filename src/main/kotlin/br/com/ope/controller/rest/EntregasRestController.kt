package br.com.ope.controller.rest

import br.com.ope.model.Entrega
import br.com.ope.repository.EntregaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(value = ["/rest/entregas", "/api/v1/entregas"])
class EntregasRestController {

    private val entregaRepository : EntregaRepository

    constructor(entregaRepository: EntregaRepository) {
        this.entregaRepository = entregaRepository
    }

    @GetMapping
    fun entregas(status: Entrega.Status?) :  MutableList<Entrega> {
        if (status == null) {
            return entregaRepository.findAll()
        } else {
            return entregaRepository.findAllByStatus(status)
        }
    }
    
    @GetMapping("/{id}/entregar")
    fun entregar(@PathVariable id: UUID) : ResponseEntity<Any>  {

        val optionalEntrega = entregaRepository.findById(id)

        if (!optionalEntrega.isPresent) {
            return ResponseEntity.notFound().build()
        }

        val entrega = optionalEntrega.get()

        entrega.dataEnvio = Date()

        entrega.status = Entrega.Status.REALIZADA

        entregaRepository.save(entrega)

        return ResponseEntity.ok(entrega)
    }
    

}
