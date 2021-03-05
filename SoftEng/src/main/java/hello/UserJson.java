package hello;

import java.util.List;

public class UserJson {

	private Integer start;
	private Integer count;
	private Long total;
	private List<UserDTO> users;

	public Integer getStart() {
		return start;
	}

	public Integer getCount() {
		return count;
	}

	public Long getTotal() {
		return total;
	}

	public List<UserDTO> getUsers() {
		return users;
	}

	public UserJson(Integer s, Integer c, Long t, List<UserDTO> u) {
		this.start = s;
		this.count = c;
		this.total = t;
		this.users = u;
	}

}