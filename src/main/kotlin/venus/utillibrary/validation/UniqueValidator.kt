package venus.utillibrary.validation

import mu.KLogging
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.ReflectionUtils
import venus.searchcore.validation.Unique
import venus.searchcore.validation.UniqueService
import java.lang.RuntimeException
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Transactional(propagation = Propagation.REQUIRES_NEW)
class UniqueValidator : ConstraintValidator<Unique, Any> {

    companion object : KLogging()

    private lateinit var message: String
    private lateinit var bunchMessage: String
    private lateinit var fields: Array<String>
    private lateinit var fieldsBunch: Array<String>
    private lateinit var errorFields: Array<String>

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun initialize(constraintAnnotation: Unique) {
        message = constraintAnnotation.message
        bunchMessage = constraintAnnotation.bunchMessage
        fields = constraintAnnotation.fields
        fieldsBunch = constraintAnnotation.fieldsBunch
        errorFields = constraintAnnotation.errorFields
    }

    override fun isValid(value: Any, context: ConstraintValidatorContext): Boolean = isValidObject(value, context)

    private fun isValidObject(entity: Any, context: ConstraintValidatorContext): Boolean {

        val uniqueService = UniqueService(em, entity.javaClass)
        val isValid = fields.filter { !isValidField(entity, it, uniqueService, context) }.count() == 0

        return isValid && isValidFieldsBunch(entity, uniqueService, context)
    }

    private fun <T> isValidField(entity: Any, field: String,
                                 uniqueService: UniqueService<T>, context: ConstraintValidatorContext): Boolean {
        val fieldValue = getValue(field, entity)

        val isValid = !uniqueService.isValueExists(getValue("id", entity) as Long?, field, fieldValue)

        if (!isValid) {
            addError(context, field, "$field '$fieldValue' $message")
        }
        return isValid
    }

    private fun <T> isValidFieldsBunch(entity: Any, uniqueService: UniqueService<T>, context: ConstraintValidatorContext): Boolean {
        val fields = fieldsBunch.map { it to getValue(it, entity) }.toMap()

        val isValid = !uniqueService.isValuesBunchExists(getValue("id", entity) as Long?, fields)

        if (!isValid) {
            addErrors(context)
        }

        return isValid
    }

    private fun addErrors(context: ConstraintValidatorContext) {
        if (errorFields.isNotEmpty()) {
            Arrays.stream(errorFields).forEach { field -> addError(context, field, bunchMessage) }
        } else {
            Arrays.stream(fieldsBunch).forEach { field -> addError(context, field, bunchMessage) }
        }
    }

    private fun addError(context: ConstraintValidatorContext, fieldName: String, message: String) {
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(fieldName).addConstraintViolation()
    }

    private operator fun getValue(fieldName: String, target: Any): Any? {
        try {
            return ReflectionUtils.findField(target.javaClass, fieldName)?.let {
                it.isAccessible = true
                it.get(target)
            }
        } catch (e: Exception) {
            logger.error(e.message, e)
            throw RuntimeException(e.message, e)
        }
    }
}
