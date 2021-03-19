package com.muffin.reditclone.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.muffin.reditclone.model.Post;
import com.muffin.reditclone.model.Subreddit;
import com.muffin.reditclone.model.dto.SubredditDto;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

	@Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
	SubredditDto mapSubredditToDto(Subreddit subreddit);
	
	default Integer mapPosts(List<Post> numberOfPosts) {
		return numberOfPosts.size();
	}
	
	@InheritInverseConfiguration
	Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
