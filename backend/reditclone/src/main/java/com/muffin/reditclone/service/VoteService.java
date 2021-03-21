package com.muffin.reditclone.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muffin.reditclone.exception.PostNotFoundException;
import com.muffin.reditclone.exception.SpringRedditException;
import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.Vote;
import com.muffin.reditclone.model.VoteType;
import com.muffin.reditclone.model.dto.VoteDto;
import com.muffin.reditclone.repository.PostRepository;
import com.muffin.reditclone.repository.VoteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {
	
	private final PostRepository postRepository;
	private final VoteRepository voteRepository;
	private final AuthService authService;

	@Transactional
	public void vote(VoteDto voteDto) {
		
		Post post = postRepository.findById(voteDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException("Post Not Found iwth ID - " + voteDto.getPostId()));
		Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		
		if (voteByPostAndUser.isPresent() && 
				voteByPostAndUser.get().getVoteType()
				.equals(voteDto.getVoteType())) {
			
			throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
		}
		
		if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
			
			post.setVoteCount(post.getVoteCount() + 1);
		}else {
			
			post.setVoteCount(post.getVoteCount() - 1);
		}
		
		voteRepository.save(mapToVote(voteDto, post));
		postRepository.save(post);
	}

	private Vote mapToVote(VoteDto voteDto, Post post) {
		return Vote.builder()
				.voteType(voteDto.getVoteType())
				.post(post)
				.user(authService.getCurrentUser())
				.build();
	}

}
