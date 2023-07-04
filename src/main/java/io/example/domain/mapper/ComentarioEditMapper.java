package io.example.domain.mapper;

import io.example.domain.dto.EditComentarioRequest;
import io.example.domain.model.Comentario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;


@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface ComentarioEditMapper {
	
	Comentario create(EditComentarioRequest request);

  @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
  void update(EditComentarioRequest request, @MappingTarget Comentario comentario);

}
