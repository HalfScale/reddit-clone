package com.muffin.reditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.muffin.reditclone.model.Comment;
import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.User;
import com.muffin.reditclone.model.dto.CommentsDto;

@Mapper(componentModel = "spring")
public interface CommentsMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "text", source = "commentsDto.text")
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "post", source = "post")
	@Mapping(target = "user", source = "user")
	Comment map(CommentsDto commentsDto, Post post, User user);
	
	@Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
	@Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
	CommentsDto mapToDto(Comment comment);
}
