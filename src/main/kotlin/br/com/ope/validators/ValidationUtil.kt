package br.com.ope.validators

import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors
import org.springframework.validation.beanvalidation.SpringValidatorAdapter
import javax.validation.Validation

object ValidatorUtil {
    //private val LOGGER = LoggerFactory.getLogger(ValidatorUtil::class.java)
    private val javaxValidator = Validation.buildDefaultValidatorFactory().validator
    private val validator = SpringValidatorAdapter(javaxValidator)

    fun validate(entry: Any): BindingResult {
        val errors = BeanPropertyBindingResult(entry, entry.javaClass.name)
        validator.validate(entry, errors)
        return errors
        /*if (errors.equals(null) || errors.allErrors.isEmpty())
            return entry
        else {
            LOGGER.error(errors.toString())
            throw InvalidDataException(errors!!.allErrors!!.toString(), errors)
        }*/
    }

    fun validate(entry: Any, errors : Errors): Errors {
        validator.validate(entry, errors)
        return errors
        /*if (errors.equals(null) || errors.allErrors.isEmpty())
            return entry
        else {
            LOGGER.error(errors.toString())
            throw InvalidDataException(errors!!.allErrors!!.toString(), errors)
        }*/
    }
}