package br.com.ope.repository

import br.com.ope.model.Arquivo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ArquivoRepository : JpaRepository<Arquivo, UUID> {

}
