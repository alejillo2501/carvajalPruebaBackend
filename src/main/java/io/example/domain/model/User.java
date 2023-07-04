package io.example.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Getter @Setter
public class User extends ComparableEntity implements UserDetails {
	
  private static final long serialVersionUID = 1L;

  @Id
  private ObjectId id;

  @CreatedDate
  private LocalDateTime createdAt;
  @LastModifiedDate
  private LocalDateTime modifiedAt;

  private boolean enabled = true;

  @Indexed(unique = true)
  private String username;
  private String password;
  @Indexed
  private String fullName;
  private Set<Role> authorities = new HashSet<>();
  
  private String token;

  public String getToken() {
	return token;
}

public void setToken(String token) {
	this.token = token;
}

public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }
  
  public void setUsername(String username) {
	  this.username = username;
  };
  
  public void setPassword(String password) {
	  this.password = password;
  };

  @Override
  public boolean isAccountNonExpired() {
    return enabled;
  }

  @Override
  public boolean isAccountNonLocked() {
    return enabled;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return enabled;
  }
  
  public String getPassword() {
	  return this.password;
  };
  
  public String getUsername() {
	  return this.username;
  };
  
  public Set<Role> getAuthorities() {
	  return this.authorities;
  };
  
  public void setEnabled(boolean enabled) {
	  this.enabled = enabled;
  };
  
  public boolean isEnabled() {
	  return this.enabled;
  };
  
  public ObjectId getId() {
	  return this.id;
  };
}
