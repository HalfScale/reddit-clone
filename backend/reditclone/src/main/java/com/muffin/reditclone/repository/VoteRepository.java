package com.muffin.reditclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.User;
import com.muffin.reditclone.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	/**
	 * Get the most current vote
	 * @param post
	 * @param currentUser
	 * @return Optional<Vote>
	 */
	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
