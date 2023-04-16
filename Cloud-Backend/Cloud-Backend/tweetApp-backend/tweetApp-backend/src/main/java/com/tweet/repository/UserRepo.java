package com.tweet.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import com.tweet.model.Users;


@Repository
public class UserRepo {

	@Autowired
	private DynamoDBMapper mapper;

	public Users findByUsername(String username) {
		return mapper.load(Users.class, username);
	}
	
	public Users findByEmail(String email) {
		return mapper.load(Users.class, email);
	}

	public Users save(Users user) {
		mapper.save(user);
		return user;
	}

	public List<Users> findAll() {
		return mapper.scan(Users.class, new DynamoDBScanExpression());
	}

	public Users searchByUsername(String username) {
		List<Users> results = mapper.scan(Users.class, new DynamoDBScanExpression());
		Users user = new Users();
		for (int i = 0; i < results.size(); i++) {
			if (results.get(i).getUsername().contains(username)) {
				user = results.get(i);
			}
		}
		return user;
	}
}
