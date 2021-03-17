package com.muffin.reditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.Subreddit;
import com.muffin.reditclone.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	
	List<Post> findAllBySubreddit(Subreddit subreddit);
	
	List<Post> findByUser(User user);
}
