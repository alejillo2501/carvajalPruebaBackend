package io.example.service;

import io.example.domain.dto.ComentarioView;
import io.example.domain.dto.EditComentarioRequest;
import io.example.domain.dto.Page;
import io.example.domain.dto.SearchComentarioQuery;
import io.example.domain.mapper.ComentarioEditMapper;
import io.example.domain.mapper.ComentarioViewMapper;
import io.example.repository.UserRepo;
import io.example.repository.ComentarioRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class ComentarioService {
	
  @Autowired
  private ComentarioRepo comentarioRepo;
  
  @Autowired
  private UserRepo userRepo;
  
  @Autowired
  private ComentarioEditMapper comentarioEditMapper;
  
  @Autowired
  private ComentarioViewMapper comentarioViewMapper;

  @Transactional
  public ComentarioView create(EditComentarioRequest request) {
    var comentario = comentarioEditMapper.create(request);
    comentario = comentarioRepo.save(comentario);
    return comentarioViewMapper.toComentarioView(comentario);
  }

  @Transactional
  public ComentarioView update(ObjectId id, EditComentarioRequest request) {
    var comentario = comentarioRepo.getById(id);
    comentarioEditMapper.update(request, comentario);
    comentario = comentarioRepo.save(comentario);
    return comentarioViewMapper.toComentarioView(comentario);
  }

  @Transactional
  public ComentarioView delete(ObjectId id) {
    var comentario = comentarioRepo.getById(id);

    comentarioRepo.delete(comentario);

    return comentarioViewMapper.toComentarioView(comentario);
  }

  public ComentarioView getComentario(ObjectId id) {
    var comentario = comentarioRepo.getById(id);
    return comentarioViewMapper.toComentarioView(comentario);
  }

  public List<ComentarioView> getComentario(Iterable<ObjectId> ids) {
    var comentario = comentarioRepo.findAllById(ids);
    return comentarioViewMapper.toComentarioView(comentario);
  }

  public List<ComentarioView> searchComentario(Page page, SearchComentarioQuery query) {
    return comentarioViewMapper.toComentarioView(comentarioRepo.searchComentario(page, query));
  }

}
