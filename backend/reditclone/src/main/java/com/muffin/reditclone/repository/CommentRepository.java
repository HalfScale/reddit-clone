package com.muffin.reditclone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muffin.reditclone.model.Comment;
import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findByPost(Post post);
	
	List<Comment> findAllByUser(User user);
}
