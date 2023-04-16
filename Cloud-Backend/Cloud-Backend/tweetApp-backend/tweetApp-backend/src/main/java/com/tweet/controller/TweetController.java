package com.tweet.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweet.exceptions.TweetDoesNotExistException;
import com.tweet.model.Reply;
import com.tweet.model.Tweet;
import com.tweet.model.TweetUpdate;
import com.tweet.model.Users;
import com.tweet.repository.UserRepo;
import com.tweet.service.TweetService;
import com.tweet.service.UserService;

@CrossOrigin(origins="*")
@RestController
//@RequestMapping("/api/v1.0/tweets")
public class TweetController {
 
	@Autowired
	private TweetService tweetService;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserService userService;

//	@Autowired
//	private Producer kafkaProducer;

	//Post new tweet

	@PostMapping("/{username}/add")
	public ResponseEntity<String> postTweet(@PathVariable String username, @RequestBody Tweet tweet, 
			@RequestHeader("Authorization") String jwt) {

		Users user= this.userRepo.findByUsername(username);

		if(user==null){
			return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
		}
		if(jwt.length()>0 && userService.validateJwt(jwt).isValid()){
			//kafkaProducer.sendMessage(tweet);
			tweetService.postTweet(username, tweet);

			return new ResponseEntity<>("\"Tweet created\"",HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);
	}


	//Get all tweets

	@GetMapping("/all")
	public ResponseEntity<?> getAllTweets(@RequestHeader("Authorization") String jwt){
		if(jwt.length()>0 && userService.validateJwt(jwt).isValid()) {
			return new ResponseEntity<List<Tweet>>(this.tweetService.getAllTweets(),HttpStatus.OK);
		}
		return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED); 
	}

	//Get all tweets of user

	@GetMapping( "/{username}")
	public ResponseEntity<?> getUserTweets(@PathVariable String username ) {
		Users user=userRepo.findByUsername(username);
		if(user==null){
			return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(tweetService.getUserTweets(username), HttpStatus.OK);

	}

	//update tweet

	@PutMapping( "/{username}/update/{tweetId}")
	public ResponseEntity<?> updateTweet(@PathVariable String username, @PathVariable String tweetId,
			@RequestBody TweetUpdate tweetUpdate,@RequestHeader("Authorization") String jwt) throws TweetDoesNotExistException {

		Users user=userRepo.findByUsername(username);
		if(user==null){
			return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
		}
		if(jwt.length()>0 && userService.validateJwt(jwt).isValid()) {
			return new ResponseEntity<>(tweetService.updateTweet(username,tweetId, tweetUpdate.getTweetText()), HttpStatus.OK);
		}
		return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

	}

	@DeleteMapping( "/{username}/delete/{tweetId}")
	public ResponseEntity<?> deleteTweet( @PathVariable String username,@PathVariable String tweetId,
			@RequestHeader("Authorization") String jwt) throws TweetDoesNotExistException {

		Users user= userRepo.findByUsername(username);

		if(user==null){
			return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
		}

		if(jwt.length()>0 && userService.validateJwt(jwt).isValid()){
			tweetService.deleteTweet(tweetId);
			return new ResponseEntity<>("\"Tweet deleted successfully\"", HttpStatus.OK);
		}
		return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

	}

	@PutMapping( "/{username}/like/{tweetId}")
	public ResponseEntity<?> likeATweet(@PathVariable String username, @PathVariable String tweetId,
			@RequestHeader("Authorization") String jwt) throws TweetDoesNotExistException {

		Users user= userRepo.findByUsername(username);

		if(user==null){
			return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
		}


		if(jwt.length()>0 && userService.validateJwt(jwt).isValid()){
			if(!tweetService.checkLikedOrNot(username, tweetId)){
				tweetService.likeTweet(username, tweetId);
				return new ResponseEntity<>("liked tweet", HttpStatus.OK);
			}else{
				tweetService.disLikeTweet(username, tweetId);
				return new ResponseEntity<>("Disliked tweet", HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
	}

	@PostMapping("/{username}/reply/{tweetId}")
	public ResponseEntity<?> replyToTweet(@PathVariable String username,@PathVariable String tweetId, 
			@RequestBody Reply tweetReply, @RequestHeader("Authorization") String jwt) throws TweetDoesNotExistException {
		
		Users user= userRepo.findByUsername(username);

		if(user==null){
			return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
		}

		if(jwt.length()>0 && userService.validateJwt(jwt).isValid()){
			
			tweetService.replyTweet(username, tweetId, tweetReply.getComment());
			return new ResponseEntity<>("Replied", HttpStatus.OK);
			
		}
		return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

	}
	
	@GetMapping("/find/{tweetId}")
	public ResponseEntity<?> getTweetById(@PathVariable String tweetId) throws TweetDoesNotExistException{
		System.out.print(tweetId);
		return new ResponseEntity<>(tweetService.getTweetById(tweetId),HttpStatus.OK);
	}
}
