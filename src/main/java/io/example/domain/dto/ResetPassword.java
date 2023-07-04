package io.example.domain.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

public record ResetPassword(@NotBlank String password) {
	
	@Builder
	  public ResetPassword {
	  }

	  public ResetPassword() {
	    this(null);
	  }

}
