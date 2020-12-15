package com.ftunicamp.tcc.exceptions;

import com.ftunicamp.tcc.dto.CustomErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            NegocioException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<Object> serviceHandler(Exception ex, WebRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var mensagemErro = ex.getMessage();

        if (ex instanceof BadCredentialsException) {
            mensagemErro = "Credenciais inválidas.";
            status = HttpStatus.UNAUTHORIZED;
        }

        var problema = new CustomErrorDTO();
        problema.setError(status.getReasonPhrase());
        problema.setMensagem(mensagemErro);

        return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
    }
}
