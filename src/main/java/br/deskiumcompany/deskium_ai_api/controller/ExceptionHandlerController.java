package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.dto.exception.ApiExceptionDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionDTO> handleException(Exception e){
        ApiExceptionDTO apiExceptionDto = new ApiExceptionDTO("Erro interno no Servidor. Contate o Suporte.");

        return ResponseEntity.internalServerError().body(apiExceptionDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDTO> handleApiExceptionDtoResponseEntity(
            MethodArgumentNotValidException e){

        List<String> errors = new ArrayList<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()){
            errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }

        ApiExceptionDTO apiExceptionDto = new ApiExceptionDTO(errors);
        return ResponseEntity.badRequest().body(apiExceptionDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiExceptionDTO> handleEntityNotFoundException(EntityNotFoundException e){
        ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO(e.getMessage());

        //notFound do response entity não aceita body.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiExceptionDTO);
    }

    @ExceptionHandler(BussinesException.class)
    public ResponseEntity<ApiExceptionDTO> handleBussinesException(BussinesException e){
        ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO(e.getMessage());

        return ResponseEntity.badRequest().body(apiExceptionDTO);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiExceptionDTO> handleBadCredentialsException(BadCredentialsException e){
        ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO("E-mail inexistente ou senha inválida");

        return ResponseEntity.badRequest().body(apiExceptionDTO);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiExceptionDTO> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO("O tamanho de todos os arquivos enviados ultrapassa o limite de 50MB. Verifique os anexos enviados.");

        return ResponseEntity.badRequest().body(apiExceptionDTO);
    }
}
