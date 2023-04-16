package com.tweet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tweet.model.ValidateResponse;
import com.tweet.exceptions.UserException;
import com.tweet.exceptions.UserNotFoundException;
import com.tweet.model.ChangePasswordRequest;
import com.tweet.model.LoginRequest;
import com.tweet.model.Users;
import com.tweet.repository.UserRepo;
import com.tweet.model.LoginResponse;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	

	//Register user


	public void register(Users user) throws UserException {
		if(Objects.nonNull(userRepo.findByEmail(user.getEmail())) || Objects.nonNull(userRepo.findByUsername(user.getUsername()))) {
			throw new UserException("user already present!!!");
			//System.out.println("user already present!!!");
		}
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		this.userRepo.save(user);
	}

	//login user


	public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) throws UserNotFoundException {

		LoginResponse loginResponse = new LoginResponse();


		Users user = userRepo.findByUsername(loginRequest.getUsername());


		if(!Objects.nonNull(user)) {
			throw new UserNotFoundException("UnAuthorized User!!!");
		}
		else if(!this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new UserNotFoundException("Password is wrong!!!");
		}

		//log.info("Authenticated User :: {}", user);

		final String jwt = jwtService.generateToken(user); // returning the token as response
		loginResponse.setMsg("logged In Successfully!!");
		loginResponse.setToken(jwt);
		ResponseEntity<LoginResponse> response = new ResponseEntity<LoginResponse>(loginResponse,HttpStatus.OK);

		log.info("Successfully Authenticated user!");
		log.info("-------- Exiting /login");

		return response;
	}

	//validate token

	public ValidateResponse validateJwt(String jwt){
		ValidateResponse validateResponse = new ValidateResponse("Invalid",false);
        System.out.print("jwtwttw");
		// first remove Bearer from Header
		jwt = jwt.substring(7);

		// check token
		log.info("--------JWT :: {}", jwt);

		// check the jwt is proper or not
		final Users user = userRepo.findByUsername(jwtService.extractUsername(jwt));

		// now validating the jwt
		try {
			if (jwtService.validateToken(jwt, user)) {
				validateResponse.setUsername(user.getUsername());
				validateResponse.setValid(true);
				log.info("Successfully validated the jwt and sending response back!");
			} else {
				log.error("JWT Token validation failed!");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("Exception occured whil validating JWT : Exception info : {}", e.getMessage());
		}
		log.info("-------- Exiting /validate");
		return validateResponse;
	}

	//Reset password 

	public String resetPassword(ChangePasswordRequest changePasswordRequest,String jwt,String username) {
		ValidateResponse validate = validateJwt(jwt);
		if(validate.isValid()) {
			final Users existingUser = userRepo.findByUsername(username);
	

			if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), existingUser.getPassword())) {
					return "New Password cannot be same as Old Password";
				}

			existingUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
			userRepo.save(existingUser);
			return "Password Changed Successfully";

		}

		throw new UserNotFoundException("UnAuthorized User");
	}



	//get User by username

	public Users getUserByUsername(String username,String jwt){

		ValidateResponse validate = validateJwt(jwt);
        
		if(!validate.isValid()) {
			throw new UserNotFoundException("UnAuthorized User");
		}
		
		Users user = userRepo.searchByUsername(username);
		return user;
	}

	//get all users

	public List<Users> getUsers(String jwt){
		ValidateResponse validate = validateJwt(jwt);

		if(!validate.isValid()) {
			throw new UserNotFoundException("UnAuthorized User");
		}
		List<Users> users = userRepo.findAll();

		return users;
	}


}
