package com.muffin.reditclone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muffin.reditclone.model.Subreddit;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

	Optional<Subreddit> findByName(String subredditName);
}
