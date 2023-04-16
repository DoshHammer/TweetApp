package com.tweet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.tweet.exceptions.UserNotFoundException;
import com.tweet.model.Users;
import com.tweet.repository.UserRepo;
import com.tweet.service.JwtService;
import com.tweet.service.UserService;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

	@Mock
	private UserService usersService;

	@MockBean
	UserRepo userRepo;

	@MockBean
	JwtService jwtService;
	@Autowired
	UserDetailsService usersServiceBean;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	MockMvc mockMvc;

	@Test
	void loginUserTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"user\",\"password\":\"user@012\"}").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("$.msg").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("logged In Successfully!!")).andReturn();
	}
	@Test
	void validateJwtTest() throws Exception {
		Users user = new Users();
		user.setUsername("user");
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user1234");
		user.setEmail("user@1234");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		 mockMvc.perform(MockMvcRequestBuilders.post("/validate")
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c"))
		 .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"));
	}

	@Test
	void InvalidLoginUserTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"user\",\"password\":\"user@012\"}").accept(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));

	}

	@Test
	void registerUserTest() throws Exception {
		Users user = new Users();
		user.setUsername("user");
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user1234");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"{\"username\":\"user\",\"firstName\":\"userFirstName\",\"lastName\":\"userLastName\",\"email\":\"user1234@gmail.com\",\"password\":\"user1234\",\"contactNumber\":\"9988776655\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "User registered successfully!";
		assertEquals(expected, actual);
	}

	@Test
	void checkAlreadyRegisteredUserTest() throws Exception {
		Users user = new Users();
		user.setUsername("user");
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user1234");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByEmail(Mockito.any())).thenReturn(user);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"{\"username\":\"user\",\"firstName\":\"userFirstName\",\"lastName\":\"userLastName\",\"email\":\"user1234@gmail.com\",\"password\":\"user1234\",\"contactNumber\":\"9685037015\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "user already present!!!";
		assertEquals(expected, actual);
	}

	@Test
	void resetPasswordTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername("user");
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user1234");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		when(userRepo.save(Mockito.any())).thenReturn(user);
		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/" + userName + "/forgot")
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"newPassword\":\"user12345\",\"confirmNewPassword\":\"user12345\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "Password Changed Successfully";
		assertEquals(expected, actual);
	}

	@Test
	void getAllUsersTest() throws Exception {
		String userName = "username";
		Users user = new Users();
		user.setUsername(userName);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		mockMvc.perform(MockMvcRequestBuilders.get("/users/all").header("Authorization",
				"12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c"))
				.andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

	}

	@Test
	void getByUserNameTest() throws Exception {
		Users user = new Users();
		String userName = "user";
		user.setUsername(userName);
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setPassword("user1234");
		user.setEmail("user1234@gmail.com");
		user.setContactNumber("9685037015");
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		when(userRepo.searchByUsername(Mockito.any())).thenReturn(user);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/search/" + userName)
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c"))
				.andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "{\"username\":\"user\",\"firstName\":\"userFirstName\",\"lastName\":\"userLastName\",\"email\":\"user1234@gmail.com\",\"password\":\"user1234\",\"contactNumber\":\"9685037015\"}";
		assertEquals(expected, actual);
	}

	@Test
	void checkInvalidGetByUserNameTest() throws Exception {
		String userName = "user";
		when(jwtService.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/search/" + userName)
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c"))
				.andReturn();
		String actual = result.getResponse().getContentAsString();
		String expected = "UnAuthorized User";
		assertEquals(expected, actual);
	}

}
