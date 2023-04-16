package com.tweet.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.tweet.model.Tweet;
import com.tweet.model.Users;
import com.tweet.model.ValidateResponse;
import com.tweet.repository.TweetRepo;
import com.tweet.repository.UserRepo;
import com.tweet.service.TweetService;
import com.tweet.service.UserService;

@AutoConfigureMockMvc
@SpringBootTest
class TweetControllerTest {

	@MockBean
	UserService usersService;

	@MockBean
	UserRepo userRepo;

	@MockBean
	TweetRepo tweetRepo;

	@MockBean
	TweetService tweetService;
	@Autowired
	MockMvc mockMvc;

	@Test
	void addTweetTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/" + userName + "/add")
				.sessionAttr("userName", userName).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
				.content("{\"tweetText\":\"welcome\"}").accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "\"Tweet created\"";
		assertEquals(expected, actual);
	}

	@Test
	void addUserNotFoundTweetTest() throws Exception {
		String userName = "user";
		when(userRepo.findByUsername(Mockito.any())).thenReturn(null);
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/" + userName + "/add")
				.sessionAttr("userName", userName).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
				.content("{\"tweetText\":\"welcome\"}").accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "User not found";
		assertEquals(expected, actual);
	}

	@Test
	void addUserNotFoundTweetTest_Unauthorized() throws Exception {
		String userName = "user";
		when(userRepo.findByUsername(Mockito.any())).thenReturn(new Users());
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post("/" + userName + "/add")
				.sessionAttr("userName", userName).contentType(MediaType.APPLICATION_JSON).header("Authorization", "")
				.content("{\"tweetText\":\"welcome\"}").accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "Unauthorized";
		assertEquals(expected, actual);
	}

	@Test
	void getAllTweetsTest() throws Exception {
		String userName = "user";
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);

		mockMvc.perform(MockMvcRequestBuilders.get("/all")
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists());

	}

	@Test
	void getAllTweetsTest_Unauthorized() throws Exception {
		String userName = "user";
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);

		MvcResult result1 = mockMvc.perform(
				MockMvcRequestBuilders.get("/all").header("Authorization", "").accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "Unauthorized";
		assertEquals(expected, actual);

	}

	@Test
	void getUsersTweetsTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		List<Tweet> tweets = new ArrayList<Tweet>();
		Tweet tweet = new Tweet();
		tweet.setUsername(userName);
		tweet.setTweetText("Hii");
		tweets.add(tweet);
		when(tweetService.getUserTweets(Mockito.any())).thenReturn(tweets);
		mockMvc.perform(MockMvcRequestBuilders.get("/" + userName).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(MockMvcResultMatchers.jsonPath("$").exists()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].username").isNotEmpty());

	}

	@Test
	void getValidUsersTweetsTest() throws Exception {
		String userName = "user";
		when(userRepo.findByUsername(Mockito.any())).thenReturn(null);
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/" + userName)).andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "user not found";
		assertEquals(expected, actual);
	}

	@Test
	void updateTweetsTest_UserNotFound() throws Exception {
		String userName = "user";

		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);

		when(userRepo.findByUsername(Mockito.any())).thenReturn(null);
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders
				.put("/" + userName + "/update/a1331033-f4ac-496a-9092-79366d960750").sessionAttr("userName", userName)
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
				.contentType(MediaType.APPLICATION_JSON).content("{\"tweetText\":\"welcome\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "user not found";
		assertEquals(expected, actual);
	}

	@Test
	void updateTweetsTest_Unauthorized() throws Exception {
		String userName = "user";

		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);

		when(userRepo.findByUsername(Mockito.any())).thenReturn(new Users());
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders
				.put("/" + userName + "/update/a1331033-f4ac-496a-9092-79366d960750").sessionAttr("userName", userName)
				.header("Authorization", "").contentType(MediaType.APPLICATION_JSON)
				.content("{\"tweetText\":\"welcome\"}").accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "Unauthorized";
		assertEquals(expected, actual);
	}

	@Test
	void updateTweetsTest() throws Exception {

		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		Tweet tweet = new Tweet();
		tweet.setUsername("user");
		tweet.setTweetId("1");
		when(tweetRepo.findTweetByUsernameAndTweetId(Mockito.any(), Mockito.any())).thenReturn(tweet);
		when(tweetRepo.save(Mockito.any())).thenReturn(tweet);
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders
				.put("/" + userName + "/update/a1331033-f4ac-496a-9092-79366d960750").sessionAttr("userName", userName)
				.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
				.contentType(MediaType.APPLICATION_JSON).content("{\"tweetText\":\"welcome\"}")
				.accept(MediaType.APPLICATION_JSON)).andReturn();
	}



	@Test
	void deleteTweetTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.delete("/" + userName + "/delete/a1331033-f4ac-496a-9092-79366d960750")
						.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
						.sessionAttr("userName", userName))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "\"Tweet deleted successfully\"";
		assertEquals(expected, actual);
	}

	@Test
	void deleteTweetTest_Unauthorized() throws Exception {
		String userName = "user";
		when(userRepo.findByUsername(Mockito.any())).thenReturn(new Users());
		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.delete("/" + userName + "/delete/a1331033-f4ac-496a-9092-79366d960750")
						.header("Authorization", "").sessionAttr("userName", userName))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "Unauthorized";
		assertEquals(expected, actual);
	}

	@Test
	void deleteTweetTest_UserNotFound() throws Exception {
		String userName = "user";
		when(userRepo.findByUsername(Mockito.any())).thenReturn(null);
		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.delete("/" + userName + "/delete/a1331033-f4ac-496a-9092-79366d960750")
						.header("Authorization", "").sessionAttr("userName", userName))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "User not found";
		assertEquals(expected, actual);
	}


	@Test
	void likeTweetTest2() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);
		when(tweetService.checkLikedOrNot(Mockito.any(), Mockito.any())).thenReturn(true);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.put("/" + userName + "/like/a1331033-f4ac-496a-9092-79366d960750")
						.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
						.sessionAttr("userName", userName))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "Disliked tweet";
		assertEquals(expected, actual);
	}

	@Test
	void likeTweetTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.put("/" + userName + "/like/a1331033-f4ac-496a-9092-79366d960750")
						.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
						.sessionAttr("userName", userName))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "liked tweet";
		assertEquals(expected, actual);
	}

	@Test
	void likeTweetTest_Unauthorized() throws Exception {
		String userName = "user";
		when(userRepo.findByUsername(Mockito.any())).thenReturn(new Users());
		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.put("/" + userName + "/like/a1331033-f4ac-496a-9092-79366d960750")
						.header("Authorization", "").sessionAttr("userName", userName))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "Unauthorized";
		assertEquals(expected, actual);
	}

	@Test
	void userNameNotFoundTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(null);

		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.put("/" + userName + "/like/a1331033-f4ac-496a-9092-79366d960750")
						.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
						.sessionAttr("userName", userName))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "User not found";
		assertEquals(expected, actual);
	}

	@Test
	void replyTweetTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(user);
		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.post("/" + userName + "/reply/a1331033-f4ac-496a-9092-79366d960750")
						.sessionAttr("userName", userName).contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
						.content("{\"comment\":\"welcome\"}").accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "Replied";
		assertEquals(expected, actual);
	}

	@Test
	void replyTweetTest_Unauthorized() throws Exception {
		String userName = "user";

		when(userRepo.findByUsername(Mockito.any())).thenReturn(new Users());
		MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders
				.post("/" + userName + "/reply/a1331033-f4ac-496a-9092-79366d960750").sessionAttr("userName", userName)
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", "")
				.content("{\"comment\":\"welcome\"}").accept(MediaType.APPLICATION_JSON)).andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "Unauthorized";
		assertEquals(expected, actual);
	}

	@Test
	void replyUserNameNotFoundTest() throws Exception {
		String userName = "user";
		Users user = new Users();
		user.setUsername(userName);
		ValidateResponse validateResponse = new ValidateResponse();
		validateResponse.setValid(true);
		validateResponse.setUsername(userName);
		when(usersService.validateJwt(Mockito.any())).thenReturn(validateResponse);
		when(userRepo.findByUsername(Mockito.any())).thenReturn(null);

		MvcResult result1 = mockMvc
				.perform(MockMvcRequestBuilders.post("/" + userName + "/reply/a1331033-f4ac-496a-9092-79366d960750")
						.sessionAttr("userName", userName).contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "12400f74dc4d8d69b713b1fe53f371c25a28a8c5fac2a91eea1f742ab4567c9c")
						.content("{\"comment\":\"welcome\"}").accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String actual = result1.getResponse().getContentAsString();
		String expected = "User not found";
		assertEquals(expected, actual);
	}


	@Test
	void getTweetByIdTest() throws Exception {
		String tweetId = "1";
		Tweet tweet = new Tweet();
		tweet.setTweetText("Hello");
		tweet.setUsername("user");
		ArrayList<String> likes = new ArrayList<String>();
		likes.add("user");
		tweet.setLikes(likes);
		Optional<Tweet> tweetOptional = Optional.of(tweet);
		when(tweetRepo.findById(Mockito.any())).thenReturn(tweetOptional);

		mockMvc.perform(MockMvcRequestBuilders.get("/find/" + tweetId));

	}
}
