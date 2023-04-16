package com.tweet.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "tweets")
public class Tweet {

	@Id
	@DynamoDBHashKey(attributeName = "tweetId")
	private String tweetId;
	
	@DynamoDBAttribute(attributeName = "username")
	private String username;
	
	@DynamoDBAttribute(attributeName = "tweetText")
	private String tweetText; 
	

	@DynamoDBAttribute(attributeName = "tweetDate")
	private String tweetDate;
	
	@DynamoDBAttribute(attributeName = "likes")
	private List<String> likes = new ArrayList<>();
	
	@DynamoDBAttribute(attributeName = "comments")
	private List<Comment> comments = new ArrayList<>();
}
