package hello;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.CascadeType;

import java.util.List;
import java.util.ArrayList;

@Data
@Entity
public class Shop {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String address;
	private Double lng;
	private Double lat;
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable
	(
	 name = "has_shop_tag",
	 joinColumns = @JoinColumn(name = "S_id"),
	 inverseJoinColumns = @JoinColumn(name = "T_id")
	)
	private List<Shop_Tag> tags;
	private Boolean withdrawn;	

	Shop (String name, String address, Double lng, Double lat, List<Shop_Tag> tags, Boolean withdrawn) {
		this.name = name;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
		this.tags = tags;
		this.withdrawn = withdrawn;
	}

	public Shop(){};

	public ShopDTO _convertToShopDTO() {
		List<String> tagList = new ArrayList<String>();
		for (Shop_Tag t: tags) {
			tagList.add(t.getName());
		}
		return new ShopDTO(id, name, address, lng, lat,
				tagList, withdrawn);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public List<Shop_Tag> getTags() {
		return tags;
	}

	public void setTags(List<Shop_Tag> tags) {
		this.tags = tags;
	}

	public void setTagsFromString(List<String> tags) {
		this.tags.clear();
		for (String s: tags) {
			this.tags.add(new Shop_Tag(s));
		}
	}

	public Boolean getWithdrawn() {
		return withdrawn;
	}

	public void setWithdrawn(Boolean withdrawn) {
		this.withdrawn = withdrawn;
	}
}
