package hello;

import java.util.List;

public class PriceJson {

	private Integer start;
	private Integer count;
	private Long total;
	private List<PriceFinal> prices;

	public Integer getStart() {
		return start;
	}

	public Integer getCount() {
		return count;
	}

	public Long getTotal() {
		return total;
	}

	public List<PriceFinal> getPrices() {
		return prices;
	}

	public PriceJson(Integer s, Integer c, Long t, List<PriceFinal> p) {
		this.start = s;
		this.count = c;
		this.total = t;
		this.prices = p;
	}

}
