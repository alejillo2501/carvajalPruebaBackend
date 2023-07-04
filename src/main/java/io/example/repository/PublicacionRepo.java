package io.example.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import io.example.domain.dto.Page;
import io.example.domain.dto.SearchPublicacionQuery;
import io.example.domain.exception.NotFoundException;
import io.example.domain.model.Publicacion;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public interface PublicacionRepo extends MongoRepository<Publicacion, ObjectId>, PublicacionRepoCustom {
	
  default Publicacion getById(ObjectId id) {
    return findById(id).orElseThrow(() -> new NotFoundException(Publicacion.class, id));
  }

  List<Publicacion> findAllById(Iterable<ObjectId> ids);
}

interface PublicacionRepoCustom {

  List<Publicacion> searchPublicacion(Page page, SearchPublicacionQuery query);
}

@RequiredArgsConstructor
class PublicacionRepoCustomImpl implements PublicacionRepoCustom {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public List<Publicacion> searchPublicacion(Page page, SearchPublicacionQuery query) {
    var operations = new ArrayList<AggregationOperation>();

    var criteriaList = new ArrayList<Criteria>();
    if (StringUtils.hasText(query.id())) {
      criteriaList.add(Criteria.where("id").is(new ObjectId(query.id())));
    }
    if (StringUtils.hasText(query.creatorId())) {
      criteriaList.add(Criteria.where("creatorId").is(new ObjectId(query.creatorId())));
    }
    if (query.createdAtStart() != null) {
      criteriaList.add(Criteria.where("createdAt").gte(query.createdAtStart()));
    }
    if (query.createdAtEnd() != null) {
      criteriaList.add(Criteria.where("createdAt").lt(query.createdAtEnd()));
    }
    if (StringUtils.hasText(query.title())) {
      criteriaList.add(Criteria.where("title").regex(query.title(), "i"));
    }    
    
    if (query.publishDateStart() != null) {
      criteriaList.add(Criteria.where("publishDate").gte(query.publishDateStart()));
    }
    if (query.publishDateEnd() != null) {
      criteriaList.add(Criteria.where("publishDate").lt(query.publishDateEnd()));
    }
    if (!criteriaList.isEmpty()) {
      var publicacionCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
      operations.add(match(publicacionCriteria));
    }

    criteriaList = new ArrayList<Criteria>();
    if (StringUtils.hasText(query.authorId())) {
      criteriaList.add(Criteria.where("user._id").is(new ObjectId(query.authorId())));
    }
    if (StringUtils.hasText(query.authorFullName())) {
      criteriaList.add(Criteria.where("user.fullName").regex(query.authorFullName(), "i"));
    }
    if (!criteriaList.isEmpty()) {
      var publicacionCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
      operations.add(lookup("users", "authorId", "_id", "user"));
      operations.add(unwind("user", false));
      operations.add(match(publicacionCriteria));
    }

    operations.add(sort(Sort.Direction.DESC, "createdAt"));
    operations.add(skip((page.number() - 1) * page.limit()));
    operations.add(limit(page.limit()));   

    var aggregation = newAggregation(Publicacion.class, operations);    
    var results = mongoTemplate.aggregate(aggregation, Publicacion.class);
    return results.getMappedResults();
  }

}
