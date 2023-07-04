package io.example.domain.mapper;

import java.util.List;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import io.example.domain.dto.ComentarioView;
import io.example.domain.dto.UserView;
import io.example.domain.model.Comentario;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public abstract class ComentarioViewMapper {
	
  private UserViewMapper userViewMapper;

  @Autowired
  public void setUserViewMapper(UserViewMapper userViewMapper) {
    this.userViewMapper = userViewMapper;
  }

  public abstract ComentarioView toComentarioView(Comentario comentario);

  public abstract List<ComentarioView> toComentarioView(List<Comentario> comentarios);
  

}
