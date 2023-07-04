package io.example.domain.mapper;


import io.example.domain.dto.EditPublicacionRequest;
import io.example.domain.model.Publicacion;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;


@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface PublicacionEditMapper {
	
  Publicacion create(EditPublicacionRequest request);

  @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
  void update(EditPublicacionRequest request, @MappingTarget Publicacion publicacion);

}
