package com.tweet.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;
import com.tweet.model.ChangePasswordRequest;
import com.tweet.model.LoginRequest;
import com.tweet.model.LoginResponse;
import com.tweet.model.Users;
import com.tweet.service.UserService;
import com.tweet.model.ValidateResponse;


@CrossOrigin(origins="*")
@RestController
//@RequestMapping("/api/v1.0/tweets")
public class UserController {

	@Autowired 
	private UserService userService;
	
    //register user
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody Users user) {
		this.userService.register(user);
		
		return new ResponseEntity<>("User registered successfully!",HttpStatus.CREATED);
	}
	
	
	//Login User
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest ) {
		
		ResponseEntity<LoginResponse> response = this.userService.login(loginRequest);
		
		return response;
	}
	
	
	//validate token
	
	@PostMapping("/validate")
	public ResponseEntity<ValidateResponse> validateJwt(@RequestHeader("Authorization") String jwt) {

		ValidateResponse response = this.userService.validateJwt(jwt);
		return new ResponseEntity<ValidateResponse>(response,HttpStatus.OK);
	}
	
	//forgot password
	
	@PostMapping("{username}/forgot")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
			@RequestHeader("Authorization") String jwt,@PathVariable String username){
		
		String response = this.userService.resetPassword(changePasswordRequest,jwt,username);
		
		return new ResponseEntity<String>(response,HttpStatus.OK);
	}
	
	
	//get user by username
	
	@GetMapping("/user/search/{username}")
	public ResponseEntity<Users> getUserByUsername(@PathVariable("username") String loginId,@RequestHeader("Authorization") String jwt){
		Users user = this.userService.getUserByUsername(loginId,jwt);
		
		return new ResponseEntity<Users>(user,HttpStatus.OK);
	}
	
	//get all users
	
	@GetMapping("/users/all")
	public ResponseEntity<List<Users>> getUsers(@RequestHeader("Authorization") String jwt){
		List<Users> users = this.userService.getUsers(jwt);
		
		return new ResponseEntity<List<Users>>(users,HttpStatus.OK);
	}
	
}
