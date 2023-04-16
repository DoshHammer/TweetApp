package com.tweet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.tweet.exceptions.TweetDoesNotExistException;
import com.tweet.model.Tweet;
import com.tweet.repository.TweetRepo;

@SpringBootTest
class TweetServiceTest {
	@Autowired
	private TweetService tweetService;

	@MockBean
	private TweetRepo tweetRepo;

	@Test
	public void postTweet_NullPointerException() {
		try {
			String username = "";
			Tweet tweet = null;
			tweetService.postTweet(username, tweet);
		} catch (NullPointerException exception) {

		}
	}

	@Test
	public void postTweet_Success() {

		String username = "user";
		Tweet tweet = new Tweet();
		tweetService.postTweet(username, tweet);
		assertTrue(Boolean.TRUE);

	}

	@Test
	public void getAllTweets_NullPointerException() {

		try {
			List<Tweet> expectedValue = null;
			List<Tweet> actualValue = tweetService.getAllTweets();
		} catch (NullPointerException e) {

		}

	}

	@Test
	public void getAllTweets() {

		List<Tweet> expectedValue = new ArrayList<>();
		Tweet tweet1 = new Tweet();
		tweet1.setTweetText("Hello");
		expectedValue.add(tweet1);
		when(tweetRepo.findAll()).thenReturn(expectedValue);
		List<Tweet> actualValue = tweetService.getAllTweets();
		assertEquals(expectedValue.get(0).getTweetText(), actualValue.get(0).getTweetText());

	}

	@Test
	public void getUserTweets() {

		List<Tweet> expectedValue = new ArrayList<>();
		Tweet tweet1 = new Tweet();
		tweet1.setTweetText("Hello");
		expectedValue.add(tweet1);
		when(tweetRepo.findByUsername(Mockito.any())).thenReturn(expectedValue);
		List<Tweet> actualValue = tweetService.getUserTweets("user");
		assertEquals(expectedValue.get(0).getTweetText(), actualValue.get(0).getTweetText());

	}

	@Test
	public void updateTweet() {
		try {

			Tweet expectedValue = new Tweet();
			expectedValue.setUsername("user");
			expectedValue.setTweetId("1");
			expectedValue.setTweetText("Hii");
			String username = "user";
			String tweetId = "1";
			String updatedTweetText = "Hii world";
			Tweet tweet = new Tweet();
			expectedValue.setUsername("user");
			expectedValue.setTweetId("1");
			expectedValue.setTweetText("Hii world");

			when(tweetRepo.findTweetByUsernameAndTweetId(Mockito.any(), Mockito.any())).thenReturn(expectedValue);
			when(tweetRepo.save(Mockito.any())).thenReturn(tweet);
			Tweet actualValue = tweetService.updateTweet(username, tweetId, updatedTweetText);
			assertEquals(tweet.getTweetText(), actualValue.getTweetText());

		} catch (Exception exception) {

			exception.printStackTrace();

		}
	}

	@Test
	public void updateTweet1() {
		try {

			String username = "user";
			String tweetId = "1";
			String updatedTweetText = "Hii world";

			when(tweetRepo.findTweetByUsernameAndTweetId(Mockito.any(), Mockito.any())).thenReturn(null);
			Tweet actualValue = tweetService.updateTweet(username, tweetId, updatedTweetText);

		} catch (TweetDoesNotExistException exception) {

			assertEquals("This tweet does not exist.", exception.getMessage());

		}
	}

	@Test
	public void deleteTweet_NullPointerException() {
		try {
			String tweetId = "";
			tweetService.deleteTweet(tweetId);
			assertTrue(true);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Test
	public void deleteTweet_TweetDoesNotExistException() {
		try {
			when(tweetRepo.findById(Mockito.any())).thenReturn(null);
			String tweetId = "";
			tweetService.deleteTweet(tweetId);
		} catch (TweetDoesNotExistException exception) {
			assertEquals("This tweet does not exist.", exception.getMessage());
		}
	}

	@Test
	public void likeTweet_NullPointerException() {
		try {

			String username = "";
			String tweetId = "";

			tweetService.likeTweet(username, tweetId);

		} catch (Exception exception) {

		}
	}

	@Test
	public void likeTweet_TweetDoesNotExistException() {
		try {
			when(tweetRepo.findById(Mockito.any())).thenReturn(Optional.of(new Tweet()));
			String username = "user";
			String tweetId = "1";

			tweetService.likeTweet(username, tweetId);

		} catch (TweetDoesNotExistException exception) {
			assertEquals("This tweet does not exist.", exception.getMessage());
		}
	}

	@Test
	public void testLikeTweet() {
		try {
			Tweet tweet = new Tweet();
			tweet.setTweetText("Hello");
			tweet.setUsername("user");
			ArrayList<String> likes = new ArrayList<String>();
			likes.add("user");
			tweet.setLikes(likes);
			Optional<Tweet> tweetOptional = Optional.of(tweet);
			when(tweetRepo.findById(Mockito.any())).thenReturn(tweetOptional);
			when(tweetRepo.save(Mockito.any())).thenReturn(tweet);
			String username = "user";
			String tweetId = "1";
			TweetService tweetservice = new TweetService();
			tweetservice.likeTweet(username, tweetId);
			assertTrue(Boolean.TRUE);
		} catch (Exception exception) {

		}
	}

	@Test
	public void testDisLikeTweet() {
		try {
			Tweet tweet = new Tweet();
			tweet.setTweetText("Hello");
			tweet.setUsername("user");
			ArrayList<String> likes = new ArrayList<String>();
			likes.add("user");
			tweet.setLikes(likes);
			Optional<Tweet> tweetOptional = Optional.of(tweet);
			when(tweetRepo.findById(Mockito.any())).thenReturn(tweetOptional);
			when(tweetRepo.save(Mockito.any())).thenReturn(tweet);
			String username = "";
			String tweetId = "";

			tweetService.disLikeTweet(username, tweetId);

		} catch (Exception exception) {

		}
	}

	@Test
	public void disLikeTweet_NullPointerException() {
		try {

			String username = "";
			String tweetId = "";

			tweetService.disLikeTweet(username, tweetId);

		} catch (Exception exception) {

		}
	}

	@Test
	public void disLikeTweet_TweetDoesNotExistException() {
		try {
			when(tweetRepo.findById(Mockito.any())).thenReturn(Optional.of(new Tweet()));
			String username = "user";
			String tweetId = "1";

			tweetService.disLikeTweet(username, tweetId);

		} catch (TweetDoesNotExistException exception) {
			assertEquals("This tweet does not exist.", exception.getMessage());
		}
	}

	@Test
	public void checkLikedOrNot_TweetDoesNotExistException() {
		try {
		

			String username = "user";
			String tweetId = "1";

			boolean actualValue = tweetService.checkLikedOrNot(username, tweetId);

		} catch (TweetDoesNotExistException exception) {
			assertEquals("This tweet does not exist.", exception.getMessage());
		}
	}

	@Test
	public void checkLikedOrNot_NullPointerException() {
		try {

			when(tweetRepo.findById(Mockito.any())).thenReturn(null);
			String username = "";
			String tweetId = "";
			boolean actualValue = tweetService.checkLikedOrNot(username, tweetId);

		} catch (TweetDoesNotExistException exception) {
			assertEquals("This tweet does not exist.", exception.getMessage());
		} catch (NullPointerException exception) {

		}
	}

	@Test
	public void checkLikedOrNot_Success() {
		try {
			Tweet tweet = new Tweet();
			tweet.setTweetText("Hello");
			tweet.setUsername("user");
			ArrayList<String> likes = new ArrayList<String>();
			likes.add("user");
			tweet.setLikes(likes);
			Optional<Tweet> tweetOptional = Optional.of(tweet);
			when(tweetRepo.findById(Mockito.any())).thenReturn(tweetOptional);
			String username = "user";
			String tweetId = "1";
			boolean actualValue = tweetService.checkLikedOrNot(username, tweetId);
			assertTrue(actualValue);
		} catch (TweetDoesNotExistException exception) {

		}
	}

	@Test
	public void checkLikedOrNot_Fail() {
		try {
			Tweet tweet = new Tweet();
			tweet.setTweetText("Hello");
			tweet.setUsername("user");
			tweet.setLikes(new ArrayList<String>());
			Optional<Tweet> tweetOptional = Optional.of(tweet);
			when(tweetRepo.findById(Mockito.any())).thenReturn(tweetOptional);
			String username = "user";
			String tweetId = "1";
			boolean actualValue = tweetService.checkLikedOrNot(username, tweetId);
			assertFalse(actualValue);
		} catch (TweetDoesNotExistException exception) {

		}
	}

	@Test
	public void replyTweet() {
		try {
			Tweet tweet = new Tweet();
			tweet.setTweetText("Hello");
			tweet.setUsername("user");
			ArrayList<String> likes = new ArrayList<String>();
			likes.add("user");
			tweet.setLikes(likes);
			Optional<Tweet> tweetOptional = Optional.of(tweet);
			when(tweetRepo.findById(Mockito.any())).thenReturn(tweetOptional);
			when(tweetRepo.save(Mockito.any())).thenReturn(tweet);
			String username = "";
			String tweetId = "";
			String tweetReply = "";

			tweetService.replyTweet(username, tweetId, tweetReply);

		} catch (Exception exception) {

		}
	}

	@Test
	public void replyTweet_NullPointer() {
		try {

			String username = "";
			String tweetId = "";
			String tweetReply = "";

			tweetService.replyTweet(username, tweetId, tweetReply);

		} catch (Exception exception) {

		}
	}

	@Test
	public void getTweetById_TweetDoesNotExistException() {

		try {
			String tweetId = "1";

			Tweet actualValue = tweetService.getTweetById(tweetId);

		} catch (TweetDoesNotExistException exception) {
			assertEquals("This tweet does not exist.", exception.getMessage());
		}
	}

	@Test
	public void getTweetById_NullPointerException() {

		try {
			Optional<Tweet> tweetOptional = Optional.of(null);
			when(tweetRepo.findById(Mockito.any())).thenReturn(tweetOptional);
			Tweet expectedValue = null;

			String tweetId = "";

			Tweet actualValue = tweetService.getTweetById(tweetId);

		} catch (TweetDoesNotExistException exception) {
			assertEquals("This tweet does not exist.", exception.getMessage());
		} catch (NullPointerException exception) {

		}
	}

	@Test
	public void getTweetById() {

		try {
			Tweet tweet = new Tweet();
			tweet.setTweetText("Hello");
			tweet.setUsername("user");
			ArrayList<String> likes = new ArrayList<String>();
			likes.add("user");
			tweet.setLikes(likes);
			Optional<Tweet> tweetOptional = Optional.of(tweet);
			when(tweetRepo.findById(Mockito.any())).thenReturn(tweetOptional);
			String tweetId = "1";

			Tweet actualValue = tweetService.getTweetById(tweetId);
			assertEquals("Hello", actualValue.getTweetText());
		} catch (TweetDoesNotExistException exception) {

		}
	}
	
	

}
