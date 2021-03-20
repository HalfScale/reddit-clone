package com.muffin.reditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muffin.reditclone.exception.PostNotFoundException;
import com.muffin.reditclone.exception.SubredditNotFoundException;
import com.muffin.reditclone.mapper.PostMapper;
import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.Subreddit;
import com.muffin.reditclone.model.User;
import com.muffin.reditclone.model.dto.PostRequest;
import com.muffin.reditclone.model.dto.PostResponse;
import com.muffin.reditclone.repository.PostRepository;
import com.muffin.reditclone.repository.SubredditRepository;
import com.muffin.reditclone.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

	private final PostRepository postRepository;
	private final SubredditRepository subredditRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final PostMapper postMapper;
	
	@Transactional(readOnly = true)
	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new PostNotFoundException(id.toString()));
		return postMapper.mapToDto(post);
		
	}
	
	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		return postRepository.findAll()
				.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}
	
	public Post save(PostRequest postRequest) {
		
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
			.orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
		
		User user = authService.getCurrentUser();
		
		log.info("Subreddit name: {}, User: {}", subreddit.getName(), user.getUsername());
		
		return postRepository.save(postMapper.map(postRequest, subreddit, user));
	}
	
	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long subredditId) {
		
		Subreddit subreddit = subredditRepository.findById(subredditId)
				.orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
		List<Post> posts = postRepository.findAllBySubreddit(subreddit);
		return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<PostResponse> getPostsByUsername(String username) {
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		return postRepository.findByUser(user)
				.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
	}
}
