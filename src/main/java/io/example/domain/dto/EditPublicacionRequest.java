package io.example.domain.dto;

import lombok.Builder;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;

import io.example.domain.model.User;

import java.time.LocalDate;
import java.util.List;

public record EditPublicacionRequest (
  
  @NotNull String title,
  String about,
  String authorId,
  String autor,
  LocalDate publishDate) {

  @Builder
  public EditPublicacionRequest {
	  
  }  

  public EditPublicacionRequest() {
    this(null, null, null, null, null);
  }
}
