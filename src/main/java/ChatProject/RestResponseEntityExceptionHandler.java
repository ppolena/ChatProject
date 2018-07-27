package ChatProject;

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
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        RepositoryConstraintViolationException exception =
                (RepositoryConstraintViolationException) ex;
        if(exception.getErrors().getFieldError().getDefaultMessage().equals(Response.EmptyName)){
            return new ResponseEntity<>(
                    new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                                new HttpHeaders(),
                                HttpStatus.BAD_REQUEST);
        }
        if(exception.getErrors().getFieldError().getDefaultMessage().equals(Response.EmptyStatus)){
            return new ResponseEntity<>(
                    new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                                new HttpHeaders(),
                                HttpStatus.BAD_REQUEST);
        }
        if(exception.getErrors().getFieldError().getDefaultMessage().equals(Response.InvalidChannelIdEdit)){
            return new ResponseEntity<>(
                    new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                    new HttpHeaders(),
                    HttpStatus.UNAUTHORIZED);
        }
        if(exception.getErrors().getFieldError().getDefaultMessage().equals(Response.InvalidNameEdit)){
            return new ResponseEntity<>(
                    new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                    new HttpHeaders(),
                    HttpStatus.UNAUTHORIZED);
        }
        if(exception.getErrors().getFieldError().getDefaultMessage().equals(Response.InvalidDateOfCreationEdit)){
            return new ResponseEntity<>(
                    new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                    new HttpHeaders(),
                    HttpStatus.UNAUTHORIZED);
        }
        if(exception.getErrors().getFieldError().getDefaultMessage().equals(Response.InvalidDateOfClosingEdit)){
            return new ResponseEntity<>(
                    new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                    new HttpHeaders(),
                    HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(
                new Error(  exception.getErrors().getFieldError().getDefaultMessage()),
                            new HttpHeaders(),
                            HttpStatus.CONFLICT);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request){
        return new ResponseEntity<>(
                            new Error(Response.NoSuchStatus),
                            new HttpHeaders(),
                            HttpStatus.NOT_FOUND);
    }
}
