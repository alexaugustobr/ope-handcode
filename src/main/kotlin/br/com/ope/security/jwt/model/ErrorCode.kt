package br.com.ope.security.jwt.model

import com.fasterxml.jackson.annotation.JsonValue

enum class ErrorCode private constructor(@get:JsonValue
                                         val errorCode: Int) {
    GLOBAL(2), AUTHENTICATION(10), JWT_TOKEN_EXPIRED(11)
}
