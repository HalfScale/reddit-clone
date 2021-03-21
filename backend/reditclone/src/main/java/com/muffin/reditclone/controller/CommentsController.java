package com.muffin.reditclone.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muffin.reditclone.model.dto.CommentsDto;
import com.muffin.reditclone.service.CommentsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
	
	private final CommentsService commentsService;

	@PostMapping
	public ResponseEntity<?> createComment(@RequestBody CommentsDto commentsDto) {
		commentsService.save(commentsDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/by-post/{postId}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
		return ResponseEntity.ok(commentsService.getAllCommentsForPost(postId));
	}
	
	@GetMapping("/by-user/{userName}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName) {
		return ResponseEntity.ok(commentsService.getAllCommentsForUser(userName));
	}
}
