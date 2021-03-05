package hello;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Price {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="price")
	private Double price;

	@Column(name="P_id")
	private Integer productId;

	@Column(name="S_id")
	private Integer shopId;

	@Column(name="date")
	private Date date;

	Price (Double price, Integer productId, Integer shopId, Date date) {
		this.price = price;
		this.productId = productId;
		this.shopId = shopId;
		this.date = date;
	}

	public Price(){};

	public PriceDTO _convertToPriceDTO() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return new PriceDTO(id, price, formatter.format(date), productId, shopId);
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

	public Date getDate() {
		return date;
	}

	public void setDate (Date date) {
		this.date = date;
	}

}
