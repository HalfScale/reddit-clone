package com.muffin.reditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.muffin.reditclone.exception.PostNotFoundException;
import com.muffin.reditclone.mapper.CommentsMapper;
import com.muffin.reditclone.model.Comment;
import com.muffin.reditclone.model.NotificationEmail;
import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.User;
import com.muffin.reditclone.model.dto.CommentsDto;
import com.muffin.reditclone.repository.CommentRepository;
import com.muffin.reditclone.repository.PostRepository;
import com.muffin.reditclone.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentsService {
	
	private static final String POST_URL = "";
	
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final CommentsMapper commentsMapper;
	private final CommentRepository commentsRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;

	public void save(CommentsDto commentsDto) {
		
		Post post = postRepository.findById(commentsDto.getPostId())
			.orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
		Comment comment = commentsMapper.map(commentsDto, post, authService.getCurrentUser());
		commentsRepository.save(comment);
		
		String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
		sendCommentNotification(message, post.getUser());
	}

	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
		
	}

	public List<CommentsDto> getAllCommentsForPost(Long postId) {
		
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
		return commentsRepository.findByPost(post)
				.stream()
				.map(commentsMapper::mapToDto)
				.collect(Collectors.toList());
	}

	public List<CommentsDto> getAllCommentsForUser(String userName) {
		
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException(userName));
		return commentsRepository.findAllByUser(user)
					.stream()
					.map(commentsMapper::mapToDto)
					.collect(Collectors.toList());
		
	}
}
