package br.com.ope.service

import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindingResult

abstract class DomainService {

    fun getBiddingErrorsForClass(o : Any?): BindingResult {

        var className = ""

        if (o != null) {
            className = o!!.toString()
        }

        return BeanPropertyBindingResult(o, className)

    }

}