package io.example.domain.mapper;

import io.example.domain.dto.PublicacionView;
import io.example.domain.dto.UserView;
import io.example.domain.model.Publicacion;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public abstract class PublicacionViewMapper {
	
  private UserViewMapper userViewMapper;

  @Autowired
  public void setUserViewMapper(UserViewMapper userViewMapper) {
    this.userViewMapper = userViewMapper;
  }

  //@Mapping(source = "authorId", target = "creator", qualifiedByName = "idToUserView")
  public abstract PublicacionView toPublicacionView(Publicacion publicacion);

  public abstract List<PublicacionView> toPublicacionView(List<Publicacion> publicaciones);
  
  /*@Named("idToUserView")
  protected UserView idToUserView(ObjectId id) {
    return userViewMapper.toUserViewById(id);
  }*/

}
