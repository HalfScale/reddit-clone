package com.muffin.reditclone.service;


import java.time.Instant;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muffin.reditclone.exception.SpringRedditException;
import com.muffin.reditclone.model.NotificationEmail;
import com.muffin.reditclone.model.User;
import com.muffin.reditclone.model.VerificationToken;
import com.muffin.reditclone.model.dto.AuthenticationResponse;
import com.muffin.reditclone.model.dto.LoginRequest;
import com.muffin.reditclone.model.dto.RefreshTokenRequest;
import com.muffin.reditclone.model.dto.RegisterRequest;
import com.muffin.reditclone.repository.UserRepository;
import com.muffin.reditclone.repository.VerificationTokenRepository;
import com.muffin.reditclone.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;
	
	@Value("${mail.app.url}")
	private String mailAppUrl;

	@Transactional
	public void signup(RegisterRequest registerRequest) {
		
		User user = new User();
		
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepository.save(user);
		
		String token = generateVerificationToken(user);
		mailService.sendMail(new NotificationEmail("Please activate your account", user.getEmail(), mailAppUrl + "/api/auth/accountVerification/" + token));
	}
	
	@Transactional(readOnly = true)
	public User getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)
				SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findByUsername(principal.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
	}

	private String generateVerificationToken(User user) {
		
		String token = UUID.randomUUID().toString();
		
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		
		verificationTokenRepository.save(verificationToken);
		
		return token;
	}

	public void verifyAccount(String token) {
		
		VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
				.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		
		fetchUserAndEnable(verificationToken);
	}

	private void fetchUserAndEnable(VerificationToken verificationToken) {
		
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
		user.setEnabled(true);
		userRepository.save(user);
		
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		
		Authentication authenticate = authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		
		String token = jwtProvider.generateToken(authenticate);
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(loginRequest.getUsername())
				.build();
	}

	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(refreshTokenRequest.getUsername())
				.build();
	}
}
