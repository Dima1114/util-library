package venus.utillibrary.handler

import org.springframework.data.rest.core.RepositoryConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import venus.utillibrary.enumeration.exception.ResourceNotFoundException
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class RestResponseEntityExceptionHandler {

    @ExceptionHandler(RepositoryConstraintViolationException::class)
    fun handleRepositoryException(ex: RepositoryConstraintViolationException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(getErrors(ex.errors), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFoundException(exception: ResourceNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(HttpStatus.NOT_FOUND, exception.message)
        return ResponseEntity(errorResponse, errorResponse.status)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintException(ex: ConstraintViolationException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity(getConstraints(ex), HttpStatus.BAD_REQUEST)
    }
}