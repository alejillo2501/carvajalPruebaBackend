package io.example.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.api.data.PublicacionTestDataFactory;
import io.example.domain.dto.PublicacionView;
import io.example.domain.dto.ListResponse;
import io.example.domain.dto.SearchPublicacionQuery;
import io.example.domain.dto.SearchRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static io.example.util.JsonHelper.fromJson;
import static io.example.util.JsonHelper.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestPublicacionSearchApi {
  private final MockMvc mockMvc;
  private final ObjectMapper objectMapper;
  private final PublicacionTestDataFactory publicacionTestDataFactory;

  @Autowired
  public TestPublicacionSearchApi(MockMvc mockMvc,
                             ObjectMapper objectMapper,
                             PublicacionTestDataFactory publicacionTestDataFactory) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
    this.publicacionTestDataFactory = publicacionTestDataFactory;
  }

  @Test
  public void testSearch() throws Exception {
    PublicacionView publicacion1 = publicacionTestDataFactory.createPublicacion("Publicacion Search A Publicacion", null, null,
    		"Publicacion Search Genre A");
    PublicacionView publicacion2 = publicacionTestDataFactory.createPublicacion("Publicacion Search B Publicacion");
    PublicacionView publicacion3 = publicacionTestDataFactory.createPublicacion("Publicacion Search C Publicacion");
    PublicacionView publicacion4 = publicacionTestDataFactory.createPublicacion("Publicacion Search D Publicacion");
    PublicacionView publicacion5 = publicacionTestDataFactory.createPublicacion("Publicacion Search E Publicacion");

    List<String> publicacionIds1 = List.of(publicacion1.id(), publicacion2.id(), publicacion3.id());
    List<String> publicacionIds2 = List.of(publicacion4.id(), publicacion5.id());


    testIdFilter(publicacion1.id());   

    publicacionTestDataFactory.deletePublicacion(publicacion1.id());
    publicacionTestDataFactory.deletePublicacion(publicacion2.id());
    publicacionTestDataFactory.deletePublicacion(publicacion3.id());
    publicacionTestDataFactory.deletePublicacion(publicacion4.id());
    publicacionTestDataFactory.deletePublicacion(publicacion5.id());
  }

  private void testIdFilter(String id) throws Exception {
    SearchPublicacionQuery query;
    ListResponse<PublicacionView> publicacionViewList;

    // Search query with publicacion id equal
    query = new  SearchPublicacionQuery(id, null, null, null, null, null, null, null, null);
    publicacionViewList = execute("/api/publicacion/search", query);
    assertEquals(1, publicacionViewList.items().size(), "Invalid search result!");
  }

  private ListResponse<PublicacionView> execute(String url, SearchPublicacionQuery query) throws Exception {
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
