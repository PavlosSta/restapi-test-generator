package hello;

import java.util.List;

public class ShopJson {
	private Integer start;
	private Integer count;
	private Long total;
	private List<ShopDTO> shops;

	public Integer getStart() {
		return start;
	}

	public Integer getCount() {
		return count;
	}

	public Long getTotal() {
		return total;
	}

	public List<ShopDTO> getShops() {
		return shops;
	}

	public ShopJson(Integer s, Integer c, Long t, List<ShopDTO> p) {
		this.start = s;
		this.count = c;
		this.total = t;
		this.shops = p;
	}
}
