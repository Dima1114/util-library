package venus.utillibrary.handler

import org.springframework.validation.Errors
import javax.validation.ConstraintViolationException

fun getErrors(errors: Errors): Map<String, Any> {
    val errorsMap = errors.fieldErrors
            .map {
                mapOf("entity" to it.objectName,
                        "field" to it.field,
                        "rejectValue" to it.rejectedValue,
                        "defaultMessage" to it.defaultMessage)
            }
    return mapOf("errors" to errorsMap)
}

fun getConstraints(exception: ConstraintViolationException): Map<String, Any> {
    val errorsMap = exception.constraintViolations
            .map {
                mapOf("entity" to it.rootBean::class.java.simpleName,
                        "field" to it.propertyPath.toString(),
                        "defaultMessage" to it.messageTemplate)
            }
    return mapOf("errors" to errorsMap)
}