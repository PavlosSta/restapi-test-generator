package hello;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Token {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name="role")
	private String role;

    @Column(name="token")
    private String token;

	Token (String role, String token) {
        this.role = role;
        this.token = token;
    }
    
    Token (Integer id, String role, String token) {
        this.id = id;
        this.role = role;
        this.token = token;
	}

	public Token(){};

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

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
