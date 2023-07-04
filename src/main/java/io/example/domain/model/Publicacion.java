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

@Document(collection = "publicacion")
@Getter @Setter
public class Publicacion extends ComparableEntity {	
 
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

  private String title;
  private String about;
  private LocalDate publishDate;

  private ObjectId authorId;
  private String user;
  
  public ObjectId getId() {
	  return this.id;
  };

}
