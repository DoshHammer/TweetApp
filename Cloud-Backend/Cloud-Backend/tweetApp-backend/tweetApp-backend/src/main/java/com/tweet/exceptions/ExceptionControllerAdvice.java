package com.tweet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingRequestHeaderException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import com.tweet.model.ValidateResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(value = UserException.class)
    public final ResponseEntity<String> handleUserException(UserException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = UserNotFoundException.class)
    public final ResponseEntity<String> handleUserException(UserNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> validationExceptions(MethodArgumentNotValidException ex) {
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			log.error(error.getDefaultMessage()); 
		});
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value =TweetDoesNotExistException.class)
	public ResponseEntity<String> TweetDoesNotExistException(TweetDoesNotExistException ex) {
		
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = MissingRequestHeaderException.class)
	public ResponseEntity<Object> missingRequestHeaderException(MissingRequestHeaderException e) {
		log.error(e.getMessage());
		ValidateResponse validateResponse = new ValidateResponse("Invalid", false);
		return new ResponseEntity<>(validateResponse, HttpStatus.OK);
	}
	
	@ExceptionHandler(value = SignatureException.class)
	public ResponseEntity<Object> signatureException(SignatureException e) {
		log.error(e.getMessage());
		ValidateResponse validateResponse = new ValidateResponse("Invalid", false);
		return new ResponseEntity<>(validateResponse, HttpStatus.OK);
	}
	
	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<Object> expiredJwtException(ExpiredJwtException e) {
		log.error(e.getMessage());
		ValidateResponse validateResponse = new ValidateResponse("Invalid", false);
		return new ResponseEntity<>(validateResponse, HttpStatus.OK);
	}
	
	@ExceptionHandler(value =Exception.class)
    public final ResponseEntity<String> handleException(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
