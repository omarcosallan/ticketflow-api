package dev.marcos.ticketflow_api.exception;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import dev.marcos.ticketflow_api.dto.exception.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {

        ProblemDetail problem = new ProblemDetail(
                "Erro de autenticação",
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                getRequestPath(request));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(HttpServletRequest request) {

        ProblemDetail problem = new ProblemDetail(
                "Falha na autenticação",
                "Email ou senha inválidos",
                HttpStatus.UNAUTHORIZED.value(),
                getRequestPath(request));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationDeniedException(HttpServletRequest request) {

        ProblemDetail problem = new ProblemDetail(
                "Permissão necessária",
                "Você não tem permissão para acessar este recurso",
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String detail = String.format(
                "O parâmetro '%s' possui o valor inválido '%s'. Tipo esperado: %s",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido");

        ProblemDetail problem = new ProblemDetail(
                "Parâmetro inválido",
                detail,
                HttpStatus.BAD_REQUEST.value(),
                getRequestPath(request));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String detail = "O corpo da solicitação é inválido ou está malformado";

        if (ex.getCause() != null) {
            detail += ": " + ex.getCause().getMessage();
        }

        ProblemDetail problem = new ProblemDetail(
                "Solicitação JSON malformada",
                detail,
                HttpStatus.BAD_REQUEST.value(),
                getRequestPath(request));

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

    @ExceptionHandler({
            SignatureVerificationException.class,
            JWTVerificationException.class,
            JWTCreationException.class
    })
    public ResponseEntity<ProblemDetail> handleTokenException(HttpServletRequest request) {

        ProblemDetail problem = new ProblemDetail(
                "Token inválido",
                "As informações de autenticação necessárias são inválidas. O token é inválido ou expirou",
                HttpStatus.UNAUTHORIZED.value(),
                getRequestPath(request)
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(HttpServletRequest request) {
        ProblemDetail problem =
                new ProblemDetail(
                        "Erro no servidor",
                        "Ocorreu um erro inesperado",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        getRequestPath(request));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    private String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
