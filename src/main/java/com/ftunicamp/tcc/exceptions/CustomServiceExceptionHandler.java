package com.ftunicamp.tcc.exceptions;

import com.ftunicamp.tcc.dto.CustomErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            NegocioException.class,
            BadCredentialsException.class,
            MaxUploadSizeExceededException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<Object> serviceHandler(Exception ex, WebRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var mensagemErro = ex.getMessage();

        if (ex instanceof BadCredentialsException) {
            mensagemErro = "Credenciais inválidas.";
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof MaxUploadSizeExceededException) {
            mensagemErro = "Tamanho de arquivo muito grande!";
            status = HttpStatus.EXPECTATION_FAILED;
        } else if (ex instanceof NoSuchElementException) {
            status = HttpStatus.NOT_FOUND;
        }

        var problema = new CustomErrorDTO();
        problema.setError(status.getReasonPhrase());
        problema.setMensagem(mensagemErro);

        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
    }
}
