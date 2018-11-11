package br.com.ope.model

import org.apache.commons.io.FilenameUtils
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.persistence.Entity

@Entity
class Arquivo : AbstractModel{
    var nome : String = ""
    var extensao : String = ""
    var mimeType : String = ""

    constructor() : super()

    class Builder{

        var arquivo = Arquivo()

        fun of(file : MultipartFile) : Builder {

            arquivo.extensao = FilenameUtils.getExtension(file.originalFilename)
            arquivo.nome = file.originalFilename!!
            arquivo.mimeType = file.contentType!!

            return this
        }

        fun build(): Arquivo {
            return arquivo
        }

        fun withId(id: UUID): Builder {
            arquivo.id = id
            return this
        }

    }
}