package io.example.api.data;

import io.example.domain.dto.ComentarioView;
import io.example.domain.dto.EditComentarioRequest;
import io.example.service.ComentarioService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
public class ComentarioTestDataFactory {
	
	@Autowired
	  private ComentarioService comentarioService;

	  public ComentarioView createComentario(
			  LocalDateTime createdAt,
			  String comentario,
			  String publicacionId,
			  String userId,
			  String user,
			  LocalDate publishDate) {
	    EditComentarioRequest createRequest = new EditComentarioRequest(
	    		createdAt, comentario, publicacionId, userId, user, publishDate
	    );

	    ComentarioView comentarioView = comentarioService.create(createRequest);

	    assertNotNull(comentarioView.id(), "Comentario id must not be null!");
	    

	    return comentarioView;
	  }

	  public ComentarioView createComentario(
			  LocalDateTime createdAt,
			  String comentario,
			  String publicacionId,
			  String userId,
			  String user) {
	    return createComentario(createdAt, comentario, publicacionId, userId, user, null);
	  }

	  public ComentarioView createComentario(
			  LocalDateTime createdAt,
			  String comentario,
			  String publicacionId,
			  String userId) {
	    return createComentario(createdAt, comentario, publicacionId, userId, null, null);
	  }

	  public ComentarioView createComentario(
			  LocalDateTime createdAt,
			  String comentario,
			  String publicacionId) {
	    return createComentario(createdAt, comentario, publicacionId, null, null, null);
	  }
	  
	  public ComentarioView createComentario(
			  LocalDateTime createdAt,
			  String comentario) {
	    return createComentario(createdAt, comentario, null, null, null, null);
	  }
	  
	  public ComentarioView createComentario(
			  LocalDateTime createdAt) {
	    return createComentario(createdAt, null, null, null, null, null);
	  }

	  public void deleteComentario(String id) {
	    comentarioService.delete(new ObjectId(id));
	  }


}
