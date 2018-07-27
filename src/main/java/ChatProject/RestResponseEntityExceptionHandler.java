package ChatProject;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends
        ResponseEntityExceptionHandler {

    @ExceptionHandler({ RepositoryConstraintViolationException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        RepositoryConstraintViolationException exception =
                (RepositoryConstraintViolationException) ex;

        System.out.println(exception.getErrors().getFieldError().getDefaultMessage());
        if(exception.getErrors().getFieldError().getDefaultMessage().equals(Response.BadNameField)){
            return new ResponseEntity<>(
                    new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                                new HttpHeaders(),
                                HttpStatus.BAD_REQUEST);
        }
        if(exception.getErrors().getFieldError().getDefaultMessage().equals(Response.BadStatusField)){
            return new ResponseEntity<>(
                    new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                    new HttpHeaders(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                            new HttpHeaders(),
                            HttpStatus.CONFLICT);
    }
}
