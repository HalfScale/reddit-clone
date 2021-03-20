package com.muffin.reditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.Subreddit;
import com.muffin.reditclone.model.User;
import com.muffin.reditclone.model.dto.PostRequest;
import com.muffin.reditclone.model.dto.PostResponse;

@Mapper(componentModel = "spring")
public interface PostMapper {

	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "user", source = "user")
	@Mapping(target = "subreddit", source = "subreddit")
	@Mapping(target = "description", source = "postRequest.description")
	Post map(PostRequest postRequest, Subreddit subreddit, User user);
	
	@Mapping(target = "id", source = "postId")
	@Mapping(target = "subredditName", source = "subreddit.name")
	@Mapping(target = "userName", source = "user.username")
	PostResponse mapToDto(Post post);
}
