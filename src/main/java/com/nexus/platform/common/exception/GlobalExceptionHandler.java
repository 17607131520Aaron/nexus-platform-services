package com.nexus.platform.common.exception;

import com.nexus.platform.common.api.ApiResponse;
import com.nexus.platform.common.api.BaseResponseCode;
import com.nexus.platform.common.api.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(
                ApiResponse.fail(ex.getCode(), "business_error", ex.getMessage(), TraceContext.getTraceId(request))
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiResponse<Void>> handleValidationException(Exception ex, HttpServletRequest request) {
        String message = BaseResponseCode.BAD_REQUEST.defaultMessage();
        if (ex instanceof MethodArgumentNotValidException manve) {
            FieldError fieldError = manve.getBindingResult().getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                message = fieldError.getDefaultMessage();
            }
        } else if (ex instanceof BindException be) {
            FieldError fieldError = be.getBindingResult().getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                message = fieldError.getDefaultMessage();
            }
        } else if (ex instanceof ConstraintViolationException cve && !cve.getConstraintViolations().isEmpty()) {
            message = cve.getConstraintViolations().iterator().next().getMessage();
        }

        return ResponseEntity.badRequest().body(
                ApiResponse.fail(BaseResponseCode.BAD_REQUEST, message, TraceContext.getTraceId(request))
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.fail(BaseResponseCode.NOT_FOUND, BaseResponseCode.NOT_FOUND.defaultMessage(), TraceContext.getTraceId(request))
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                ApiResponse.fail(BaseResponseCode.METHOD_NOT_ALLOWED, BaseResponseCode.METHOD_NOT_ALLOWED.defaultMessage(), TraceContext.getTraceId(request))
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknownException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.fail(BaseResponseCode.INTERNAL_ERROR, BaseResponseCode.INTERNAL_ERROR.defaultMessage(), TraceContext.getTraceId(request))
        );
    }
}
