package com.nsp.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "Institute")
public class Institute {

	@Email
	@NotBlank
	public String email;
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password=password;
	}


	public String password;
	
	
	public Institute() {
		super();
	}


	public Institute(@Email @NotBlank String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
}