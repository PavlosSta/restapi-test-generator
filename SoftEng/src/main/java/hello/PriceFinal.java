package hello;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PriceFinal {

	private Double price;
	private Integer productId;
	private Integer shopId;
	private String date;

	private String productName;
	private List<String> productTags;
	private String shopName;
	private List<String> shopTags;
	private String shopAddress;
	private Integer shopDist;

	PriceFinal(Double price, Integer productId, Integer shopId, String date, String productName,
			List<String> productTags, String shopName, List<String> shopTags, String shopAddress, Integer shopDist)
	{
		this.price = price;
		this.productId = productId;
		this.shopId = shopId;
		this.date = date;
		this.productName = productName;
		this.productTags = productTags;
		this.shopName = shopName;
		this.shopTags = shopTags;
		this.shopAddress = shopAddress;
		this.shopDist = shopDist;
	}

	public PriceFinal(){};

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductTags(List<String> productTags) {
		this.productTags = productTags;
	}

	public List<String> getProductTags() {
		return productTags;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}  

	public String getShopName() {
		return shopName;
	}

	public void setShopTags(List<String> shopTags) {
		this.shopTags = shopTags;
	}

	public List<String> getShopTags() {
		return shopTags;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopDist(Integer shopDist) {
		this.shopDist = shopDist;
	}

	public Integer getShopDist() {
		return shopDist;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public String getDate() {
		return date;
	}

	public void setDate (Date date) {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		this.date = simpleDateFormat.format(date);
	}

	public void setDateFromString(String date) {
		this.date = date;
	}


}
