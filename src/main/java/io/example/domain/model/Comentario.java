package io.example.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "comentarios")
@Getter @Setter
public class Comentario extends ComparableEntity {
	
  private static final long serialVersionUID = 1L;
  
  @Id
  private ObjectId id;

  @CreatedBy
  private ObjectId creatorId;
  @LastModifiedBy
  private ObjectId modifierId;

  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime modifiedAt;
  
  private String comentario;
  private LocalDate publishDate;

  private ObjectId publicacionId;
  private ObjectId userId;
  private String user;
  
  public ObjectId getId() {
	  return this.id;
  };
}
