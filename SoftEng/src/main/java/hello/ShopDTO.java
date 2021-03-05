package hello;

import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
public class ShopDTO {
	private Integer id;
	private String name;
	private String address;
	private Double lng;
	private Double lat;
	private List<String> tags;
	private Boolean withdrawn;	

	ShopDTO (Integer id, String name, String address, Double lng, Double lat, List<String> tags, Boolean withdrawn) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
		this.tags = tags;
		this.withdrawn = withdrawn;
	}

	ShopDTO (String name, String address, Double lng, Double lat, List<String> tags, Boolean withdrawn) {
		this.name = name;
		this.address = address;
		this.lng = lng;
		this.lat = lat;
		this.tags = tags;
		this.withdrawn = withdrawn;
	}

	public ShopDTO(){};

	public Boolean anyNull() {
		// if (name      == null) return true;
		// if (address   == null) return true;
		// if (lng       == null) return true;
		// if (lat       == null) return true;
		// if (tags      == null) return true;
		// if (withdrawn == null) return true;
		if (name      == null) throw new BadRequestException("name");
		if (address   == null) throw new BadRequestException("address");
		if (lng       == null) throw new BadRequestException("lng");
		if (lat       == null) throw new BadRequestException("lat");
		if (tags      == null) throw new BadRequestException("tags");
		if (withdrawn == null) throw new BadRequestException("withdrawn");
		return false;
	}

	public Shop _convertToShop() {
		List<Shop_Tag> tagList = new ArrayList<Shop_Tag>();
		for (String s : tags) {
			tagList.add(new Shop_Tag(s));
		}
		return new Shop(name, address, lng, lat,
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Boolean getWithdrawn() {
		return withdrawn;
	}

	public void setWithdrawn(Boolean withdrawn) {
		this.withdrawn = withdrawn;
	}
}
