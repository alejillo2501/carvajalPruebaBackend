package io.example.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.api.data.ComentarioTestDataFactory;
import io.example.domain.dto.ComentarioView;
import io.example.domain.dto.ListResponse;
import io.example.domain.dto.SearchComentarioQuery;
import io.example.domain.dto.SearchRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static io.example.util.JsonHelper.fromJson;
import static io.example.util.JsonHelper.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestComentarioSearchApi {	
  private final MockMvc mockMvc;
  private final ObjectMapper objectMapper;
  private final ComentarioTestDataFactory comentarioTestDataFactory;

  @Autowired
  public TestComentarioSearchApi(MockMvc mockMvc,
                             ObjectMapper objectMapper,
                             ComentarioTestDataFactory comentarioTestDataFactory) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
    this.comentarioTestDataFactory = comentarioTestDataFactory;
  }

  @Test
  public void testSearch() throws Exception {
	  
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	LocalDateTime dateTime = LocalDateTime.parse("2023-07-03T17:46:40", formatter);
    ComentarioView comentario1 = comentarioTestDataFactory.createComentario(dateTime, null, null,
    		"Comentario Search Genre A");
    ComentarioView comentario2 = comentarioTestDataFactory.createComentario(dateTime);
    ComentarioView comentario3 = comentarioTestDataFactory.createComentario(dateTime);
    ComentarioView comentario4 = comentarioTestDataFactory.createComentario(dateTime);
    ComentarioView comentario5 = comentarioTestDataFactory.createComentario(dateTime);

    List<String> comentarioIds1 = List.of(comentario1.id(), comentario2.id(), comentario3.id());
    List<String> comentarioIds2 = List.of(comentario4.id(), comentario5.id());


    testIdFilter(comentario1.id());   

    comentarioTestDataFactory.deleteComentario(comentario1.id());
    comentarioTestDataFactory.deleteComentario(comentario2.id());
    comentarioTestDataFactory.deleteComentario(comentario3.id());
    comentarioTestDataFactory.deleteComentario(comentario4.id());
    comentarioTestDataFactory.deleteComentario(comentario5.id());
  }

  private void testIdFilter(String id) throws Exception {
    SearchComentarioQuery query;
    ListResponse<ComentarioView> comentarioViewList;

    // Search query with comentario id equal
    query = new  SearchComentarioQuery(id, null, null, null, null, null, null, null, null, null);
    comentarioViewList = execute("/api/comentario/search", query);
    assertEquals(1, comentarioViewList.items().size(), "Invalid search result!");
  }

  private ListResponse<ComentarioView> execute(String url, SearchComentarioQuery query) throws Exception {
    MvcResult result = this.mockMvc
      .perform(post(url)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, new SearchRequest<>(query))))
      .andExpect(status().isOk())
      .andReturn();

    return fromJson(objectMapper,
      result.getResponse().getContentAsString(),
      new TypeReference<>() {
      });
  }


}
