package com.tweet.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.tweet.model.ValidateResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@SpringBootTest
class ExceptionControllerAdviceTest {

	@Autowired
	private ExceptionControllerAdvice e;

	@Test
	public void TweetDoesNotExistException() {

		TweetDoesNotExistException ex = new TweetDoesNotExistException("Tweet not exist");
		ResponseEntity<String> actualValue = e.TweetDoesNotExistException(ex);
		assertEquals("Tweet not exist", actualValue.getBody());

	}

	@Test
	public void signatureException() {

		SignatureException s = new SignatureException("msg");

		ResponseEntity<Object> actualValue = e.signatureException(s);
		assertTrue(actualValue.getBody() instanceof ValidateResponse);

	}

	@Test
	public void expiredJwtException() {

		ExpiredJwtException jwtException = new ExpiredJwtException(null, null, null);

		ResponseEntity<Object> actualValue = e.expiredJwtException(jwtException);

		assertTrue(actualValue.getBody() instanceof ValidateResponse);

	}

}
