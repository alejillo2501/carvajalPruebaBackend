package io.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.api.data.ComentarioTestDataFactory;
import io.example.domain.dto.ComentarioView;
import io.example.domain.dto.EditComentarioRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = Role.USER_ADMIN)
public class TestComentarioApi {
	
  private final MockMvc mockMvc;
  private final ObjectMapper objectMapper;
  private final ComentarioTestDataFactory comentarioTestDataFactory;

  @Autowired
  public TestComentarioApi(MockMvc mockMvc,
                       ObjectMapper objectMapper,
                       ComentarioTestDataFactory comentarioTestDataFactory) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
    this.comentarioTestDataFactory = comentarioTestDataFactory;
  }

  @Test
  public void testCreateSuccess() throws Exception {
	  
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	LocalDateTime dateTime = LocalDateTime.parse("2023-07-03T17:46:40", formatter);
    EditComentarioRequest goodRequest = new EditComentarioRequest(
    		dateTime,null, null, null, null,null
    		);
    		

    MvcResult createResult = this.mockMvc
      .perform(post("/api/comentario")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, goodRequest)))
      .andExpect(status().isOk())
      .andReturn();

    ComentarioView comentarioView = fromJson(objectMapper, createResult.getResponse().getContentAsString(), ComentarioView.class);
    assertNotNull(comentarioView.id(), "Comentario id must not be null!");
    assertEquals(goodRequest.comentario(), comentarioView.comentario(), "Post comentario update isn't applied!");
  }

  @Test
  public void testCreateFail() throws Exception {
    EditComentarioRequest badRequest = new EditComentarioRequest();

    this.mockMvc
      .perform(post("/api/comentario")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, badRequest)))
      .andExpect(status().isBadRequest())
      .andExpect(content().string(containsString("Method argument validation failed")));
  }

  @Test
  public void testEditSuccess() throws Exception {
	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	  LocalDateTime dateTime = LocalDateTime.parse("2023-07-03T17:46:40", formatter);
	  ComentarioView comentarioView = comentarioTestDataFactory.createComentario(dateTime);

    EditComentarioRequest updateRequest = new EditComentarioRequest(
    			dateTime,"Cool Comentario", null, null, null, null
    	    		);

    MvcResult updateResult = this.mockMvc
      .perform(put(String.format("/api/comentario/%s", comentarioView.id()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, updateRequest)))
      .andExpect(status().isOk())
      .andReturn();
    ComentarioView newComentarioView = fromJson(objectMapper, updateResult.getResponse().getContentAsString(),
    		ComentarioView.class);

    assertEquals(updateRequest.comentario(), newComentarioView.comentario(), "Comentario update isn't applied!");
    assertEquals(updateRequest.user(), newComentarioView.user(), "Comentario user update isn't applied!");
  }

  @Test
  public void testEditFailBadRequest() throws Exception {
	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	  LocalDateTime dateTime = LocalDateTime.parse("2023-07-03T17:46:40", formatter);
	  ComentarioView comentarioView = comentarioTestDataFactory.createComentario(dateTime);

	  EditComentarioRequest updateRequest = new EditComentarioRequest();

    this.mockMvc
      .perform(put(String.format("/api/comentario/%s", comentarioView.id()))
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, updateRequest)))
      .andExpect(status().isBadRequest())
      .andExpect(content().string(containsString("Method argument validation failed")));
  }

  @Test
  public void testEditFailNotFound() throws Exception {
	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	  LocalDateTime dateTime = LocalDateTime.parse("2023-07-03T17:46:40", formatter);
	  EditComentarioRequest updateRequest = new EditComentarioRequest(
			  dateTime,null, null, null, null, null
    		);

    this.mockMvc
      .perform(put(String.format("/api/comentario/%s", "5f07c259ffb98843e36a2aa9"))
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(objectMapper, updateRequest)))
      .andExpect(status().isNotFound())
      .andExpect(content().string(containsString("Entity Comentario with id 5f07c259ffb98843e36a2aa9 not found")));
  }

  @Test
  public void testDeleteSuccess() throws Exception {
	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	  LocalDateTime dateTime = LocalDateTime.parse("2023-07-03T17:46:40", formatter);
	  ComentarioView comentarioView = comentarioTestDataFactory.createComentario(dateTime);

    this.mockMvc
      .perform(delete(String.format("/api/comentario/%s", comentarioView.id())))
      .andExpect(status().isOk());

    this.mockMvc
      .perform(get(String.format("/api/comentario/%s", comentarioView.id())))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteFailNotFound() throws Exception {
    this.mockMvc
      .perform(delete(String.format("/api/comentario/%s", "5f07c259ffb98843e36a2aa9")))
      .andExpect(status().isNotFound())
      .andExpect(content().string(containsString("Entity Comentario with id 5f07c259ffb98843e36a2aa9 not found")));
  }

  @Test
  @WithAnonymousUser
  public void testGetSuccess() throws Exception {
	  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	  LocalDateTime dateTime = LocalDateTime.parse("2023-07-03T17:46:40", formatter);
	  ComentarioView comentarioView = comentarioTestDataFactory.createComentario(dateTime);

    MvcResult getResult = this.mockMvc
      .perform(get(String.format("/api/comentario/%s", comentarioView.id())))
      .andExpect(status().isOk())
      .andReturn();

    ComentarioView newComentarioView = fromJson(objectMapper, getResult.getResponse().getContentAsString(), ComentarioView.class);

    assertEquals(comentarioView.id(), newComentarioView.id(), "Comentario ids must be equal!");
  }

  @Test
  @WithAnonymousUser
  public void testGetNotFound() throws Exception {
    this.mockMvc
      .perform(get(String.format("/api/comentario/%s", "5f07c259ffb98843e36a2aa9")))
      .andExpect(status().isNotFound())
      .andExpect(content().string(containsString("Entity Comentario with id 5f07c259ffb98843e36a2aa9 not found")));
  }

}
