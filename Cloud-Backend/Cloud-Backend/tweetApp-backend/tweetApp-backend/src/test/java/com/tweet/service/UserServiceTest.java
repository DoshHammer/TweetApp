package com.tweet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tweet.exceptions.UserNotFoundException;
import com.tweet.model.ChangePasswordRequest;
import com.tweet.model.LoginRequest;
import com.tweet.model.LoginResponse;
import com.tweet.model.Users;
import com.tweet.model.ValidateResponse;
import com.tweet.repository.TweetRepo;
import com.tweet.repository.UserRepo;

@SpringBootTest
class UserServiceTest {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepo userRepo;

	@MockBean
	JwtService jwtService;

	@MockBean
	PasswordEncoder passwordEncoder;

	@Test
	public void register() {
		try {

			Users user = null;
			userService.register(user);
			assertFalse(Boolean.TRUE);
		} catch (NullPointerException exception) {
			assertTrue(Boolean.TRUE);
		}
	}

	@Test
	public void login() {

		Users user = new Users();
		user.setUsername("user");
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user@012");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("user");
		loginRequest.setPassword("user@012");
		ResponseEntity<LoginResponse> actualValue = userService.login(loginRequest);

		assertEquals("logged In Successfully!!", actualValue.getBody().getMsg());

	}

	@Test
	public void login_UserNotFoundException() {

		try {
			when(userRepo.findByUsername(Mockito.any())).thenReturn(null);
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setUsername("user");
			loginRequest.setPassword("user@012");
			ResponseEntity<LoginResponse> actualValue = userService.login(loginRequest);

		} catch (Exception e) {
			assertEquals("UnAuthorized User!!!", e.getMessage());
		}

	}

	@Test
	public void login_PasswordNotMatch() {

		try {
			Users user = new Users();
			user.setUsername("user");
			user.setFirstName("userFirstName");
			user.setLastName("userLastName");
			user.setPassword("user@012");
			user.setEmail("user1234@gmail.com");
			user.setContactNumber("9685037015");
			when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
			when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setUsername("user");
			loginRequest.setPassword("user@0123");
			ResponseEntity<LoginResponse> actualValue = userService.login(loginRequest);
		} catch (UserNotFoundException e) {
			assertEquals("Password is wrong!!!", e.getMessage());
		}

	}

	@Test
	public void validateJwt() {

		Users user = new Users();
		String userName = "user";
		user.setUsername(userName);
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user@012");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);

		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);

		String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";

		ValidateResponse actualValue = userService.validateJwt(jwt);

		assertEquals("user", actualValue.getUsername());

	}

	@Test
	public void validateJwt_InvalidToken() {

		Users user = new Users();
		String userName = "user";
		user.setUsername(userName);
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user@012");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);

		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);

		String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";

		ValidateResponse actualValue = userService.validateJwt(jwt);

		assertEquals("Invalid", actualValue.getUsername());

	}

	@Test
	public void resetPassword_PasswordSame() {
		Users user = new Users();
		String userName = "user";
		user.setUsername(userName);
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user@012");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);

		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		changePasswordRequest.setConfirmNewPassword("user@012");
		changePasswordRequest.setNewPassword("user@012");
		String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";
		String actualValue = userService.resetPassword(changePasswordRequest, jwt, "user");

		assertEquals("New Password cannot be same as Old Password", actualValue);

	}

	@Test
	public void resetPassword_UserNotFoundException() {
		try {
			Users user = new Users();
			String userName = "user";
			user.setUsername(userName);
			user.setFirstName("userFirstName");
			user.setLastName("userLastName");
			user.setPassword("user@012");
			user.setEmail("user1234@gmail.com");
			user.setContactNumber("9685037015");
			when(userRepo.findByUsername(Mockito.any())).thenReturn(user);

			when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);

			ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
			changePasswordRequest.setConfirmNewPassword("user1234");
			changePasswordRequest.setNewPassword("user1234");
			String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";

			String actualValue = userService.resetPassword(changePasswordRequest, jwt, "loki");
		} catch (UserNotFoundException e) {
			assertEquals("UnAuthorized User", e.getMessage());
		}

	}

	@Test
	public void resetPassword() {

		Users user = new Users();
		String userName = "user";
		user.setUsername(userName);
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user@012");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);

		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		String expectedValue = "";

		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		changePasswordRequest.setConfirmNewPassword("user12345");
		changePasswordRequest.setNewPassword("user12345");
		String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";

		String actualValue = userService.resetPassword(changePasswordRequest, jwt, "user");

		assertEquals("Password Changed Successfully", actualValue);

	}

	@Test
	public void getUserByUsername() {

		Users user = new Users();
		user.setUsername("user");
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user@012");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		when(userRepo.searchByUsername(Mockito.any())).thenReturn(user);
		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		
		String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";
		Users actualValue = userService.getUserByUsername("loki", jwt);
		assertEquals("userFirstName", actualValue.getFirstName());

	}

	@Test
	public void getUserByUsername_UserNotFoundException() {
		try {
			Users user = new Users();
			user.setUsername("user");
			user.setFirstName("userFirstName");
			user.setLastName("userLastName");
			user.setPassword("user@012");
			user.setEmail("user1234@gmail.com");
			user.setContactNumber("9685037015");
			when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
			when(userRepo.searchByUsername(Mockito.any())).thenReturn(user);
			when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(null);
			String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";
			Users actualValue = userService.getUserByUsername("loki", jwt);
		} catch (UserNotFoundException e) {
			assertEquals("UnAuthorized User", e.getMessage());
		}

	}

	@Test
	public void getUsers() {

		Users user = new Users();
		String userName = "user";
		user.setUsername(userName);
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user@012");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		List<Users> listUser = new ArrayList<>();
		listUser.add(user);
		when(userRepo.findAll()).thenReturn(listUser);
		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";
		List<Users> actualValue = userService.getUsers(jwt);
		assertEquals("userFirstName", actualValue.get(0).getFirstName());

	}

	@Test
	public void getUsers_UserNotFoundException() {
		try {
			Users user = new Users();
			String userName = "user";
			user.setUsername(userName);
			user.setFirstName("userFirstName");
			user.setLastName("userLastName");
			user.setPassword("user@012");
			user.setEmail("user1234@gmail.com");
			user.setContactNumber("9685037015");
			when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
			when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);
			String jwt = "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c";
			List<Users> actualValue = userService.getUsers(jwt);
		} catch (UserNotFoundException e) {
			assertEquals("UnAuthorized User", e.getMessage());
		}

	}

}
