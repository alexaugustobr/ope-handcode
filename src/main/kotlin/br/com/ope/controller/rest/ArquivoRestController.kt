package br.com.ope.controller.rest

import br.com.ope.service.FileStorage
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Controller
@RequestMapping
class ArquivoRestController {

    val fileStorage : FileStorage

    constructor(fileStorage: FileStorage) {
        this.fileStorage = fileStorage
    }

    @PostMapping("/publico/arquivos")
    fun enviarPublico(file : MultipartFile) : ResponseEntity<Any> {
        return ResponseEntity.ok(fileStorage.store(file))
    }

    @GetMapping("/publico/arquivos/{id}")
    fun receberPublico(@PathVariable id : UUID) :  ResponseEntity<Resource> {
        try {
            val file = fileStorage.loadFile(id)
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file)
        } catch (e : Exception) {
            return ResponseEntity.notFound().build()
        }
    }
}