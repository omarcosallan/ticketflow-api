package dev.marcos.ticketflow_api.exception;

import dev.marcos.ticketflow_api.dto.exception.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existingMessage, newMessage) -> existingMessage + ", " + newMessage)
        );

        ProblemDetail problem = new ProblemDetail(
                "Erro de validação",
                "Um ou mais campos são invalidos",
                HttpStatus.BAD_REQUEST.value(),
                getRequestPath(req));

        problem.setProperty("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

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
