package io.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

  private static final long serialVersionUID = 1L;
  public static final String USER_ADMIN = "USER_ADMIN";
  public static final String AUTHOR_ADMIN = "AUTHOR_ADMIN";
  public static final String BOOK_ADMIN = "BOOK_ADMIN";

  private String authority;
  
  public String getAuthority() {
	  return this.authority;
  };

}
