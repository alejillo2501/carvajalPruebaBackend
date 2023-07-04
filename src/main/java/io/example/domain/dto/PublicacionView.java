package io.example.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.example.domain.model.User;

public record PublicacionView(
	  String id,
	  //UserView creator,
	  LocalDateTime createdAt,
	  String title,
	  String about,
	  String authorId,
	  String autor,
	  LocalDate publishDate) {

}
