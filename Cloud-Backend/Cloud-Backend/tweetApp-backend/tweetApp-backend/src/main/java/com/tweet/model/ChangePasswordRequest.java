package com.tweet.model;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ChangePasswordRequest {

	@NotBlank(message = "New Password is mandatory")
	private String newPassword;

	@NotBlank(message = "Confirm New Password is mandatory")
	private String confirmNewPassword;
}
 