package com.tweet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import com.tweet.model.Tweet;

@Repository
public class TweetRepo {
	
	@Autowired
	private DynamoDBMapper mapper;
	
	public Tweet save(Tweet tweet) {
		mapper.save(tweet);
		return tweet;
	}

	public List<Tweet> findAll() {
		return mapper.scan(Tweet.class, new DynamoDBScanExpression());
	}

	public List<Tweet> findByUsername(String username) {
		List<Tweet> results = mapper.scan(Tweet.class, new DynamoDBScanExpression());
		List<Tweet> returnedList = new ArrayList<Tweet>();
		for (int i = 0; i < results.size(); i++) {
			if (results.get(i).getUsername().equals(username)) {
				returnedList.add(results.get(i));
			}
		}
		return returnedList;
	}

	

	public Optional<Tweet> findById(String tweetId) {
		Optional<Tweet> tweet = Optional.of(mapper.load(Tweet.class, tweetId));
		return tweet;
	}



	public void deleteById(String tweetId) {
		Tweet deleteTweet = mapper.load(Tweet.class, tweetId);
		mapper.delete(deleteTweet);

	}
	
	public  Tweet findTweetByUsernameAndTweetId(String username,String tweetId) {
		List<Tweet> results = mapper.scan(Tweet.class, new DynamoDBScanExpression());
		Tweet tweet = new Tweet();
		
		for (int i = 0; i < results.size(); i++) {
			if (results.get(i).getUsername().equals(username) && results.get(i).getTweetId().equals(tweetId)) {
				tweet = results.get(i);
			}
		}
		
		return tweet;
		
	}

}
