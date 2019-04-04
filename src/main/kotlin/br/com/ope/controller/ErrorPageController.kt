package br.com.ope.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest


//@Controller
class ErrorPageController : ErrorController {

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest): String {
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) ?: return "500"

        val statusCode = Integer.valueOf(status.toString())
            return when (statusCode) {
                HttpStatus.NOT_FOUND.value() -> "404"
                HttpStatus.FORBIDDEN.value() -> "403"
                else -> {
                    return "500"
                }
            }
    }

    override fun getErrorPath(): String {
        return "/error"
    }
}
