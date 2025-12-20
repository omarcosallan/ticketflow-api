package dev.marcos.ticketflow_api.exception;

import dev.marcos.ticketflow_api.dto.exception.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(
            NotFoundException ex, HttpServletRequest request) {

        ProblemDetail problem =
                new ProblemDetail(
                        "Recurso não encontrado",
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        ProblemDetail problem =
                new ProblemDetail(
                        "Violação de regra de negócio",
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Erro no servidor",
                        "Ocorreu um erro inesperado.",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    private String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
