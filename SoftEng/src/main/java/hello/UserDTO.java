package hello;

import lombok.Data;

@Data
public class UserDTO {

    private Integer id;
	private String LName;
	private String FName;
	private String username;
	private String email;
	private String password;
	private String role;


	UserDTO (Integer id, String LName, String FName, String username, String email, String password, String role) {
		this.id = id;
		this.LName = LName;
		this.FName = FName;
		this.username = username;
		this.email = email;
        this.password = password;
        this.role = role;
    }
    
    UserDTO (String LName, String FName, String username, String email, String password) {
		this.LName = LName;
		this.FName = FName;
		this.username = username;
		this.email = email;
        this.password = password;
	}

	public UserDTO(){};

	public Boolean anyNull() {
		if (LName    == null) return true;
		if (FName    == null) return true;
		if (username == null) return true;
        if (email    == null) return true;
        if (password == null) return true;
		return false;
	}

	public User _convertToUser() {
		return new User(LName, FName, username,
            email, password, "user");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public String getLName() {
		return LName;
	}

	public void setLName(String LName) {
		this.LName = LName;
    }

	public String getFName() {
		return FName;
	}

	public void setFName(String FName) {
		this.FName = FName;
	}
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
