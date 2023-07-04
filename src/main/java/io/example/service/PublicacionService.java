package io.example.service;

import io.example.domain.dto.PublicacionView;
import io.example.domain.dto.EditPublicacionRequest;
import io.example.domain.dto.Page;
import io.example.domain.dto.SearchPublicacionQuery;
import io.example.domain.mapper.PublicacionEditMapper;
import io.example.domain.mapper.PublicacionViewMapper;
import io.example.repository.UserRepo;
import io.example.repository.PublicacionRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicacionService {
	
  @Autowired
  private PublicacionRepo publicacionRepo;
  
  @Autowired
  private UserRepo userRepo;
  
  @Autowired
  private PublicacionEditMapper publicacionEditMapper;
  
  @Autowired
  private PublicacionViewMapper publicacionViewMapper;

  @Transactional
  public PublicacionView create(EditPublicacionRequest request) {
    var publicacion = publicacionEditMapper.create(request);
    publicacion = publicacionRepo.save(publicacion);
    return publicacionViewMapper.toPublicacionView(publicacion);
  }

  @Transactional
  public PublicacionView update(ObjectId id, EditPublicacionRequest request) {
    var publicacion = publicacionRepo.getById(id);
    publicacionEditMapper.update(request, publicacion);
    publicacion = publicacionRepo.save(publicacion);
    return publicacionViewMapper.toPublicacionView(publicacion);
  }

  @Transactional
  public PublicacionView delete(ObjectId id) {
    var publicacion = publicacionRepo.getById(id);

    publicacionRepo.delete(publicacion);

    return publicacionViewMapper.toPublicacionView(publicacion);
  }

  public PublicacionView getPublicacion(ObjectId id) {
    var publicacion = publicacionRepo.getById(id);
    return publicacionViewMapper.toPublicacionView(publicacion);
  }

  public List<PublicacionView> getPublicacion(Iterable<ObjectId> ids) {
    var publicacion = publicacionRepo.findAllById(ids);
    return publicacionViewMapper.toPublicacionView(publicacion);
  }

  public List<PublicacionView> searchPublicacion(Page page, SearchPublicacionQuery query) {
    return publicacionViewMapper.toPublicacionView(publicacionRepo.searchPublicacion(page, query));
  }

}
