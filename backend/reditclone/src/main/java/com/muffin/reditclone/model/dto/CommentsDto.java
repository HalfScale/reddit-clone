package com.muffin.reditclone.model.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
	private Long postId;
	private Long id;
	private Instant createdDate;
	private String text;
	private String userName;
}
