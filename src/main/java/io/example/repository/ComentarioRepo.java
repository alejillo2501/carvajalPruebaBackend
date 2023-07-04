package io.example.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.util.StringUtils;

import io.example.domain.dto.Page;
import io.example.domain.dto.SearchComentarioQuery;
import io.example.domain.exception.NotFoundException;
import io.example.domain.model.Comentario;
import lombok.RequiredArgsConstructor;

public interface ComentarioRepo extends MongoRepository<Comentario, ObjectId>, ComentarioRepoCustom{
	
	default Comentario getById(ObjectId id) {
	    return findById(id).orElseThrow(() -> new NotFoundException(Comentario.class, id));
	  }

	  List<Comentario> findAllById(Iterable<ObjectId> ids);
	}

	interface ComentarioRepoCustom {

	  List<Comentario> searchComentario(Page page, SearchComentarioQuery query);
	}

	@RequiredArgsConstructor
	class ComentarioRepoCustomImpl implements ComentarioRepoCustom {

	  @Autowired
	  private MongoTemplate mongoTemplate;

	  @Override
	  public List<Comentario> searchComentario(Page page, SearchComentarioQuery query) {
	    var operations = new ArrayList<AggregationOperation>();

	    var criteriaList = new ArrayList<Criteria>();
	    if (StringUtils.hasText(query.id())) {
	      criteriaList.add(Criteria.where("id").is(new ObjectId(query.id())));
	    }
	    if (StringUtils.hasText(query.creatorId())) {
	      criteriaList.add(Criteria.where("creatorId").is(new ObjectId(query.creatorId())));
	    }	 
	    
	    if (StringUtils.hasText(query.publicacionId())) {
	      criteriaList.add(Criteria.where("publicacionId").is(new ObjectId(query.publicacionId())));
	    }	
	    
	    if (query.createdAtStart() != null) {
	      criteriaList.add(Criteria.where("createdAt").gte(query.createdAtStart()));
	    }
	    if (query.createdAtEnd() != null) {
	      criteriaList.add(Criteria.where("createdAt").lt(query.createdAtEnd()));
	    }
	    if (StringUtils.hasText(query.comentario())) {
	      criteriaList.add(Criteria.where("comentario").regex(query.comentario(), "i"));
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
	    if (StringUtils.hasText(query.userId())) {
	      criteriaList.add(Criteria.where("user._id").is(new ObjectId(query.userId())));
	    }
	    if (StringUtils.hasText(query.user())) {
	      criteriaList.add(Criteria.where("user.fullName").regex(query.user(), "i"));
	    }
	    if (!criteriaList.isEmpty()) {
	      var publicacionCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
	      operations.add(lookup("users", "userId", "_id", "user"));
	      operations.add(unwind("user", false));
	      operations.add(match(publicacionCriteria));
	    }

	    operations.add(sort(Sort.Direction.DESC, "createdAt"));
	    operations.add(skip((page.number() - 1) * page.limit()));
	    operations.add(limit(page.limit()));   

	    var aggregation = newAggregation(Comentario.class, operations);    
	    var results = mongoTemplate.aggregate(aggregation, Comentario.class);
	    return results.getMappedResults();
	  }

}
