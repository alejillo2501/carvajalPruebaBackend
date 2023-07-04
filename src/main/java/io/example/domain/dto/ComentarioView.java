package io.example.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.example.domain.model.User;


public record ComentarioView(
	  String id,
	  LocalDateTime createdAt,
	  String comentario,
	  String publicacionId,
	  String userId,
	  String user,
	  LocalDate publishDate
		) {

}
