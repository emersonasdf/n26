package com.n26.web

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(InvalidFormatException::class)
    fun validationErrorHandler(): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY)
    }
}
