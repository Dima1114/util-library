package venus.utillibrary.handler

import org.springframework.http.HttpStatus

class ErrorResponse(val status: HttpStatus, val message: String?){

    constructor(exception: Exception) :
            this(HttpStatus.INTERNAL_SERVER_ERROR, exception.message)
}
