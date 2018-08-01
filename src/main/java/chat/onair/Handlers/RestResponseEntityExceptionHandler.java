package chat.onair.Handlers;

import chat.onair.Entities.Error;
import chat.onair.Interfaces.Response;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends
        ResponseEntityExceptionHandler {

    @ExceptionHandler({ RepositoryConstraintViolationException.class })
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {

        RepositoryConstraintViolationException exception = (RepositoryConstraintViolationException) ex;

        return new ResponseEntity<>(
                exception.getErrors().getFieldErrors(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request){

        return new ResponseEntity<>(
                new Error(Response.NoSuchStatus),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }
}
