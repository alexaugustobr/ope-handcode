package br.com.ope.dto

import java.util.*

abstract class DomainModelDTO(
        open val id : UUID? = null
) : DataTransferObject() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DomainModelDTO

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}