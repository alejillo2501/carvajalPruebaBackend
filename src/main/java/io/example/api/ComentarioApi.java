package io.example.api;

import io.example.domain.dto.UserView;
import io.example.domain.dto.ComentarioView;
import io.example.domain.dto.EditComentarioRequest;
import io.example.domain.dto.ListResponse;
import io.example.domain.dto.SearchComentarioQuery;
import io.example.domain.dto.SearchRequest;
import io.example.domain.model.Role;
import io.example.service.UserService;
import io.example.service.ComentarioService;
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

@Tag(name = "Comentarios")
@RestController
@RequestMapping(path = "api/comentario")
@RequiredArgsConstructor
public class ComentarioApi {
	
	@Autowired
	  private ComentarioService comentarioService;
	  
	  @Autowired
	  private UserService authorService;

	  @RolesAllowed({Role.USER_ADMIN, Role.AUTHOR_ADMIN})
	  @PostMapping
	  public ComentarioView create(@RequestBody @Valid EditComentarioRequest request) {
	    return comentarioService.create(request);
	  }

	  @RolesAllowed({Role.USER_ADMIN, Role.AUTHOR_ADMIN})
	  @PutMapping("{id}")
	  public ComentarioView edit(@PathVariable String id, @RequestBody @Valid EditComentarioRequest request) {
	    return comentarioService.update(new ObjectId(id), request);
	  }

	  @RolesAllowed({Role.USER_ADMIN, Role.AUTHOR_ADMIN})
	  @DeleteMapping("{id}")
	  public ComentarioView delete(@PathVariable String id) {
	    return comentarioService.delete(new ObjectId(id));
	  }

	  @GetMapping("{id}")
	  public ComentarioView get(@PathVariable String id) {
	    return comentarioService.getComentario(new ObjectId(id));
	  }	  

	  @PostMapping("search")
	  public ListResponse<ComentarioView> search(@RequestBody @Valid SearchRequest<SearchComentarioQuery> request) {
	    return new ListResponse<ComentarioView>(comentarioService.searchComentario(request.page(), request.query()));
	  }

}
