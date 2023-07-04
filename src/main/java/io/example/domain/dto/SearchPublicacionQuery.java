package io.example.domain.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record SearchPublicacionQuery(
  String id,

  String creatorId,
  LocalDateTime createdAtStart,
  LocalDateTime createdAtEnd,

  String title, 
  LocalDate publishDateStart,
  LocalDate publishDateEnd,

  String authorId,
  String authorFullName
) {
	
  @Builder
  public SearchPublicacionQuery {
  }

  public SearchPublicacionQuery() {
    this(null, null, null, null, null, null, null, null, null);
  }

}
