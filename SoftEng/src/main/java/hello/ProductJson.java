package hello;

import java.util.List;

public class ProductJson {
	private Integer start;
	private Integer count;
	private Long total;
	private List<ProductDTO> products;

	public Integer getStart() {
		return start;
	}

	public Integer getCount() {
		return count;
	}

	public Long getTotal() {
		return total;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}

	public ProductJson(Integer s, Integer c, Long t, List<ProductDTO> p) {
		this.start = s;
		this.count = c;
		this.total = t;
		this.products = p;
	}
}
