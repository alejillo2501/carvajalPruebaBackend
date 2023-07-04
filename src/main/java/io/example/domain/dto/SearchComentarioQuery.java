package io.example.domain.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SearchComentarioQuery(
	  String id,
	  String creatorId,
	  LocalDateTime createdAtStart,
	  LocalDateTime createdAtEnd,
	  String comentario,
	  LocalDate publishDateStart,
	  LocalDate publishDateEnd,
	  String publicacionId,
	  String userId,
	  String user
		) {
	
	@Builder
	  public SearchComentarioQuery {
	  }

	  public SearchComentarioQuery() {
	    this(null, null, null, null, null, null, null, null, null, null);
	  }

}
