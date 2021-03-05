package hello;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class PriceDTO {
	private Integer id;
	private Double price;
	private String dateFrom;
	private String dateTo;
	private Integer productId;
	private Integer shopId;

	PriceDTO (Integer id, Double price, String dateFrom, Integer productId, Integer shopId) {
		this.id = id;
		this.price = price;
		this.dateFrom = dateFrom;
		this.productId = productId;
		this.shopId = shopId;
	}

	PriceDTO (Integer id, Double price, String dateFrom, String dateTo, Integer productId, Integer shopId) {
		this.id = id;
		this.price = price;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.productId = productId;
		this.shopId = shopId;
	}

	PriceDTO (Double price, String dateFrom, String dateTo, Integer productId, Integer shopId) {
		this.price = price;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.productId = productId;
		this.shopId = shopId;
	}

	public PriceDTO(){};

	public boolean anyNull() {
		if (price     == null) return true;
		if (dateFrom  == null) return true;
		if (dateTo    == null) return true;
		if (productId == null) return true;
		if (shopId    == null) return true;
		return false;
	}

	public Price _convertToPrice() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dateF = null;
		Date dateT = null;
		try {
			dateF = formatter.parse(dateFrom); //(pattern = "yyyy-MM-dd")
			dateT =  formatter.parse(dateTo);
		} catch(ParseException p) {
			throw new BadRequestException("Error: Invalid date format");
		}
		return new Price(price, productId, shopId, dateF);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
}
