package com.project.Karman.exception;

import com.project.Karman.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.project.Karman.exception.ExceptionMessage.SYSTEM_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.error(e));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {

        // 미확인 내부서버 에러 위치 및 내용 확인
        log.error(e.getMessage(), e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ErrorResponse.error(SYSTEM_ERROR.getMessage(),
                SYSTEM_ERROR.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleNotValidException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    return fieldName + ": " + message;
                })
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("유효성 검증에 실패했습니다.");

        log.warn("Validation failed: {}", errorMessage);

        return ResponseEntity.badRequest().body(ErrorResponse.error(errorMessage, 400));
    }
}
