package br.com.ope.exception

open class BusinessException : Exception {

    constructor() : super()

    constructor(msg : String) : super(msg)

}