package io.example.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.api.data.PublicacionTestDataFactory;
import io.example.domain.dto.PublicacionView;
import io.example.domain.dto.EditPublicacionRequest;
import io.example.domain.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static io.example.util.JsonHelper.fromJson;
import static io.example.util.JsonHelper.toJson;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = Role.USER_ADMIN)
public class TestPublicacionApi {
	
  private final MockMvc mockMvc;
  private final ObjectMapper objectMapper;
  private final PublicacionTestDataFactory publicacionTestDataFactory;

  @Autowired
  public TestPublicacionApi(MockMvc mockMvc,
                       ObjectMapper objectMapper,
                       PublicacionTestDataFactory publicacionTestDataFactory) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
    this.publicacionTestDataFactory = publicacionTestDataFactory;
  }

  @Test
  public void testCreateSuccess() throws Exception {
    EditPublicacionRequest goodRequest = new EditPublicacionRequest(
    		"Test Publicacion A",null, null, null, null
    		);
    		

    MvcResult createResult = this.mockMvc
      .perform(post("/api/publicacion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, goodRequest)))
      .andExpect(status().isOk())
      .andReturn();

    PublicacionView publicacionView = fromJson(objectMapper, createResult.getResponse().getContentAsString(), PublicacionView.class);
    assertNotNull(publicacionView.id(), "Publicacion id must not be null!");
    assertEquals(goodRequest.title(), publicacionView.title(), "Post title update isn't applied!");
  }

  @Test
  public void testCreateFail() throws Exception {
    EditPublicacionRequest badRequest = new EditPublicacionRequest();

    this.mockMvc
      .perform(post("/api/publicacion")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, badRequest)))
      .andExpect(status().isBadRequest())
      .andExpect(content().string(containsString("Method argument validation failed")));
  }

  @Test
  public void testEditSuccess() throws Exception {
	  PublicacionView publicacionView = publicacionTestDataFactory.createPublicacion("Test Publicacion A");

    EditPublicacionRequest updateRequest = new EditPublicacionRequest(
    	    		"Test Publicacion B","Cool Publicacion", null, null, null
    	    		);

    MvcResult updateResult = this.mockMvc
      .perform(put(String.format("/api/publicacion/%s", publicacionView.id()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, updateRequest)))
      .andExpect(status().isOk())
      .andReturn();
    PublicacionView newPublicacionView = fromJson(objectMapper, updateResult.getResponse().getContentAsString(),
    		PublicacionView.class);

    assertEquals(updateRequest.title(), newPublicacionView.title(), "Publicacion title update isn't applied!");
    assertEquals(updateRequest.about(), newPublicacionView.about(), "Publicacion about update isn't applied!");
  }

  @Test
  public void testEditFailBadRequest() throws Exception {
	  PublicacionView publicacionView = publicacionTestDataFactory.createPublicacion("Test Publicación A");

    EditPublicacionRequest updateRequest = new EditPublicacionRequest();

    this.mockMvc
      .perform(put(String.format("/api/publicacion/%s", publicacionView.id()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, updateRequest)))
      .andExpect(status().isBadRequest())
      .andExpect(content().string(containsString("Method argument validation failed")));
  }

  @Test
  public void testEditFailNotFound() throws Exception {
    EditPublicacionRequest updateRequest = new EditPublicacionRequest(
    		"Test Publicacion B",null, null, null, null
    		);

    this.mockMvc
      .perform(put(String.format("/api/publicacion/%s", "5f07c259ffb98843e36a2aa9"))
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, updateRequest)))
      .andExpect(status().isNotFound())
      .andExpect(content().string(containsString("Entity Publicacion with id 5f07c259ffb98843e36a2aa9 not found")));
  }

  @Test
  public void testDeleteSuccess() throws Exception {
    PublicacionView publicacionView = publicacionTestDataFactory.createPublicacion("Test Publicacion A");

    this.mockMvc
      .perform(delete(String.format("/api/publicacion/%s", publicacionView.id())))
      .andExpect(status().isOk());

    this.mockMvc
      .perform(get(String.format("/api/publicacion/%s", publicacionView.id())))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteFailNotFound() throws Exception {
    this.mockMvc
      .perform(delete(String.format("/api/publicacion/%s", "5f07c259ffb98843e36a2aa9")))
      .andExpect(status().isNotFound())
      .andExpect(content().string(containsString("Entity Publicacion with id 5f07c259ffb98843e36a2aa9 not found")));
  }

  @Test
  @WithAnonymousUser
  public void testGetSuccess() throws Exception {
    PublicacionView publicacionView = publicacionTestDataFactory.createPublicacion("Test Publicacion A");

    MvcResult getResult = this.mockMvc
      .perform(get(String.format("/api/publicacion/%s", publicacionView.id())))
      .andExpect(status().isOk())
      .andReturn();

    PublicacionView newPublicacionView = fromJson(objectMapper, getResult.getResponse().getContentAsString(), PublicacionView.class);

    assertEquals(publicacionView.id(), newPublicacionView.id(), "Publicacion ids must be equal!");
  }

  @Test
  @WithAnonymousUser
  public void testGetNotFound() throws Exception {
    this.mockMvc
      .perform(get(String.format("/api/publicacion/%s", "5f07c259ffb98843e36a2aa9")))
      .andExpect(status().isNotFound())
      .andExpect(content().string(containsString("Entity Publicacion with id 5f07c259ffb98843e36a2aa9 not found")));
  }

}
