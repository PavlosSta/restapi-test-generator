package hello;

import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
public class ProductDTO {
	private Integer id;
	private String name;
	private String description;
	private String category;
	private List<String> tags;
	private Boolean withdrawn;


	ProductDTO (Integer id, String name, String description, String category, List<String> tags, Boolean withdrawn) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.tags = tags;
		this.withdrawn = withdrawn;
	}

	ProductDTO (String name, String description, String category, List<String> tags, Boolean withdrawn) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.tags = tags;
		this.withdrawn = withdrawn;
	}

	public ProductDTO(){};

	public Boolean anyNull() {
		if (name        == null) return true;
		if (description == null) return true;
		if (category    == null) return true;
		if (tags        == null) return true;
		if (withdrawn   == null) return true;
		return false;
	}

	public Product _convertToProduct() {
		List<Product_Tag> tagList = new ArrayList<Product_Tag>();
		for (String s : tags) {
			tagList.add(new Product_Tag(s));
		}
		return new Product(name, description, category,
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
