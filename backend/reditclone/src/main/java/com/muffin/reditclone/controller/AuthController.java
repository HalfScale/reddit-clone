package com.muffin.reditclone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffin.reditclone.model.dto.AuthenticationResponse;
import com.muffin.reditclone.model.dto.LoginRequest;
import com.muffin.reditclone.model.dto.RegisterRequest;
import com.muffin.reditclone.service.AuthService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		authService.signup(registerRequest);
		return ResponseEntity.ok("User Registration Successful!");
	}
	
	@GetMapping("/accountVerification/{token}")
	public ResponseEntity<String> verfiyAccount(@PathVariable String token) {
		authService.verifyAccount(token);
		return ResponseEntity.ok("Account Activated Successfully!");
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}
}
