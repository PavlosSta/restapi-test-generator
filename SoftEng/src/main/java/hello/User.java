package hello;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name="LName")
	private String LName;

	@Column(name="FName")
	private String FName;

	@Column(name="Username")
	private String username;

	@Column(name="Email")
	private String email;

	@Column(name="Password")
	private String password;

	@Column(name="role")
	private String role;

	User (String LName, String FName, String username, String email, String password, String role) {
		this.username = username;
		this.LName = LName;
		this.FName = FName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public User(){};

	public UserDTO _convertToUserDTO() {
		return new UserDTO(id, LName, FName, username,
            email, password, role);
	}

	public String getFName() {
		return FName;
	}

	public void setFName(String FName) {
		this.FName = FName;
	}

	public String getLName() {
		return LName;
	}

	public void setLName(String LName) {
		this.LName = LName;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
