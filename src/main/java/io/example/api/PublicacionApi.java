package io.example.api;

import io.example.domain.dto.UserView;
import io.example.domain.dto.PublicacionView;
import io.example.domain.dto.EditPublicacionRequest;
import io.example.domain.dto.ListResponse;
import io.example.domain.dto.SearchPublicacionQuery;
import io.example.domain.dto.SearchRequest;
import io.example.domain.model.Role;
import io.example.service.UserService;
import io.example.service.PublicacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Tag(name = "Publicaciones")
@RestController
@RequestMapping(path = "api/publicacion")
@RequiredArgsConstructor
public class PublicacionApi {
	
  @Autowired
  private PublicacionService publicacionService;
  
  @Autowired
  private UserService authorService;

  @RolesAllowed({Role.USER_ADMIN, Role.AUTHOR_ADMIN})
  @PostMapping
  public PublicacionView create(@RequestBody @Valid EditPublicacionRequest request) {
    return publicacionService.create(request);
  }

  @RolesAllowed({Role.USER_ADMIN, Role.AUTHOR_ADMIN})
  @PutMapping("{id}")
  public PublicacionView edit(@PathVariable String id, @RequestBody @Valid EditPublicacionRequest request) {
    return publicacionService.update(new ObjectId(id), request);
  }

  @RolesAllowed({Role.USER_ADMIN, Role.AUTHOR_ADMIN})
  @DeleteMapping("{id}")
  public PublicacionView delete(@PathVariable String id) {
    return publicacionService.delete(new ObjectId(id));
  }

  @GetMapping("{id}")
  public PublicacionView get(@PathVariable String id) {
    return publicacionService.getPublicacion(new ObjectId(id));
  }

  @GetMapping("{id}/author")
  public UserView getAuthors(@PathVariable String id) {
    return authorService.getUser(new ObjectId(id));
  }

  @PostMapping("search")
  public ListResponse<PublicacionView> search(@RequestBody @Valid SearchRequest<SearchPublicacionQuery> request) {
    return new ListResponse<PublicacionView>(publicacionService.searchPublicacion(request.page(), request.query()));
  }

}
