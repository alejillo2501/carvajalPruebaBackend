package io.example.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

public record EditComentarioRequest(
	  
	  LocalDateTime createdAt,
	  String comentario,
	  String publicacionId,
	  String userId,
	  String user,
	  LocalDate publishDate
		) {
	
	 @Builder
	  public EditComentarioRequest {
		  
	  }  

	  public EditComentarioRequest() {
	    this(null, null, null, null, null, null);
	  }


}
