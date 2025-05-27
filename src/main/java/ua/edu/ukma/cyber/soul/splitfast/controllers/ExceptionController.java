package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ErrorResponseDto;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ExceptionTranslator;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final ExceptionTranslator exceptionTranslator;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> internalServerError(Exception e) {
        return toResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> methodNotAllowed(Exception e) {
        return toResponse(HttpStatus.METHOD_NOT_ALLOWED, e);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponseDto> forbidden(Exception e) {
        return toResponse(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler({
            NoResourceFoundException.class,
            NotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> notFound(Exception e) {
        return toResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ValidationException.class
    })
    public ResponseEntity<ErrorResponseDto> badRequest(Exception e) {
        return toResponse(HttpStatus.BAD_REQUEST, e);
    }

    private <T extends Throwable> ResponseEntity<ErrorResponseDto> toResponse(HttpStatusCode code, T exception) {
        return ResponseEntity.status(code)
                .contentType(MediaType.APPLICATION_JSON)
                .body(exceptionTranslator.translate(exception));
    }
}
