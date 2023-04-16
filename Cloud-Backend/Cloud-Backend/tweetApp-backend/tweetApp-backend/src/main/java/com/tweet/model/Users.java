package com.tweet.model;


import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Data;

@Data
@DynamoDBTable(tableName = "user")
public class Users implements Serializable {
	/**
	 * 	
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@DynamoDBHashKey(attributeName = "username")
	private String username;
	
	@DynamoDBAttribute(attributeName = "firstName")
	private String firstName;
	
	@DynamoDBAttribute(attributeName = "lastName") 
	private String lastName;
	
	@DynamoDBAttribute(attributeName = "email")
	private String email;
	
	@DynamoDBAttribute(attributeName = "password")
	private String password;
	
	@DynamoDBAttribute(attributeName = "contactNumber")
	private String contactNumber;

	
}
