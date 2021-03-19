package com.muffin.reditclone.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muffin.reditclone.exception.SpringRedditException;
import com.muffin.reditclone.mapper.SubredditMapper;
import com.muffin.reditclone.model.Subreddit;
import com.muffin.reditclone.model.dto.SubredditDto;
import com.muffin.reditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
	
	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		
		Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
		subredditDto.setId(save.getId());
		return subredditDto;
	}

	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll()
			.stream()
			.map(subredditMapper::mapSubredditToDto)
			.collect(Collectors.toList());
		
	}

	public SubredditDto getSubreddit(Long id) {
		
		Subreddit subreddit = subredditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("No subreddit found with id - " + id));
		return subredditMapper.mapSubredditToDto(subreddit);
	}
}
