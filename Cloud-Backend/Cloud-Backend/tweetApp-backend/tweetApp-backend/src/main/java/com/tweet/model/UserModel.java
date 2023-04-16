package com.tweet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserModel {

	private String firstName;
	
	private String lastName;
	@Id
	private String username;
	private String email;
	private String contactNumber;
}
 