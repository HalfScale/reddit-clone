package com.muffin.reditclone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muffin.reditclone.model.dto.PostRequest;
import com.muffin.reditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	
	public void save(PostRequest postRequest) {
		subredditRepository.findByName(postRequest.getSubredditName())
			.orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
		
		authService.
		
	}
}
