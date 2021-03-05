package hello;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(String username) {
		super("User " + username + " not found");
	}

	public UserNotFoundException(Integer id) {
		super("User " + id + " not found");
	}
}