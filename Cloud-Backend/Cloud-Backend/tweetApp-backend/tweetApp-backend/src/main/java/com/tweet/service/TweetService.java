package com.tweet.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tweet.exceptions.TweetDoesNotExistException;
import com.tweet.model.Comment;
import com.tweet.model.Tweet;
import com.tweet.repository.TweetRepo;

@Service
public class TweetService {

	@Autowired
	private TweetRepo  tweetRepo;
	
	public void postTweet(String username,Tweet tweet) {
		LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        String formattedDate = myDateObj.format(myFormatObj);
        tweet.setTweetId(UUID.randomUUID().toString());

        tweet.setTweetDate(formattedDate);
                
        tweet.setUsername(username);
        tweet.setLikes(new ArrayList<String>());
        tweet.setComments(new ArrayList<Comment>());
        tweetRepo.save(tweet);
		
	}
	
	
	public List<Tweet> getAllTweets() {
        return tweetRepo.findAll();
    }


	public List<Tweet> getUserTweets(String username) {
        
            List<Tweet> tweets = tweetRepo.findByUsername(username);
            return tweets;
        }
	
	
    public Tweet updateTweet(String username, String tweetId, String updatedTweetText) throws TweetDoesNotExistException {
   
        Optional<Tweet> originalTweetOptional = Optional.ofNullable(tweetRepo.findTweetByUsernameAndTweetId(username,tweetId));
        if(originalTweetOptional.isPresent()) {
            Tweet tweet = originalTweetOptional.get();
            tweet.setTweetText(updatedTweetText);
            return tweetRepo.save(tweet);
        } else {
            throw new TweetDoesNotExistException("This tweet does not exist.");
        }
    }
    
    public void deleteTweet(String tweetId) throws TweetDoesNotExistException {
        if(tweetRepo.findById(tweetId)!=null) {
            tweetRepo.deleteById(tweetId);
        }else {

            throw new TweetDoesNotExistException("This tweet does not exist.");
        }
    }
    
    public void likeTweet(String username, String tweetId) throws TweetDoesNotExistException{
        Optional<Tweet> tweetOptional = tweetRepo.findById(tweetId);
        if(tweetOptional.isPresent()) {
            Tweet tweet = tweetOptional.get();
        	System.out.println(tweet+"............................");

            tweet.getLikes().add(username);
            tweetRepo.save(tweet);
        } else {
            throw new TweetDoesNotExistException("This tweet does not exist.");
        }
    }
    
    public void disLikeTweet(String username, String tweetId) throws TweetDoesNotExistException{
        Optional<Tweet> tweetOptional = tweetRepo.findById(tweetId);
        if(tweetOptional.isPresent()) {
            Tweet tweet = tweetOptional.get();
            tweet.getLikes().remove(username);
            tweetRepo.save(tweet);
        } else {
        	throw new TweetDoesNotExistException("This tweet does not exist.");

        }
    }

    public boolean checkLikedOrNot(String username, String tweetId) throws TweetDoesNotExistException {
        Optional<Tweet> tweetOptional = tweetRepo.findById(tweetId);

        if(!tweetOptional.isPresent()) {
        	throw new TweetDoesNotExistException("This tweet does not exist.");
        }
        
        Tweet tweet = tweetOptional.get();
        System.out.println(tweet.getLikes()+"  "+tweet.getLikes().contains(username)+" ...**********...........");
        return tweet.getLikes().contains(username);
    }
    
    public void replyTweet(String username, String tweetId, String tweetReply) throws TweetDoesNotExistException {
        Optional<Tweet> tweetOptional = tweetRepo.findById(tweetId);
        if(tweetOptional.isPresent()) {
            Tweet tweet = tweetOptional.get();
            tweet.getComments().add(new Comment(username, tweetReply));
            tweetRepo.save(tweet);
        } else {
        	throw new TweetDoesNotExistException("This tweet does not exist.");
        }
    }
    
    public Tweet getTweetById(String tweetId) throws TweetDoesNotExistException{
    	 Optional<Tweet> tweetOptional = tweetRepo.findById(tweetId);
         Tweet tweet = null;
         if(tweetOptional.isPresent()) {
             tweet = tweetOptional.get();
         } else {
         	throw new TweetDoesNotExistException("This tweet does not exist.");
         }
         return tweet;
		
    }
}
