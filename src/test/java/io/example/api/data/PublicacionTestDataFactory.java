package io.example.api.data;

import io.example.domain.dto.PublicacionView;
import io.example.domain.dto.EditPublicacionRequest;
import io.example.service.PublicacionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
public class PublicacionTestDataFactory {
	
  @Autowired
  private PublicacionService publicacionService;

  public PublicacionView createPublicacion(
		  String title,
		  String about,
		  String authorId,
		  String autor,
		  LocalDate publishDate) {
    EditPublicacionRequest createRequest = new EditPublicacionRequest(
    		title, about, authorId, autor, publishDate
    );

    PublicacionView publicacionView = publicacionService.create(createRequest);

    assertNotNull(publicacionView.id(), "Publicacion id must not be null!");
    

    return publicacionView;
  }

  public PublicacionView createPublicacion(
		  String title,
		  String about,
		  String authorId,
		  String autor) {
    return createPublicacion(title, about, authorId, autor, null);
  }

  public PublicacionView createPublicacion(
		  String title,
		  String about,
		  String authorId) {
    return createPublicacion(title, about, authorId, null, null);
  }

  public PublicacionView createPublicacion(
		  String title,
		  String about) {
    return createPublicacion(title, about, null, null, null);
  }
  
  public PublicacionView createPublicacion(
		  String title) {
    return createPublicacion(title, null, null, null, null);
  }

  public void deletePublicacion(String id) {
    publicacionService.delete(new ObjectId(id));
  }

}
