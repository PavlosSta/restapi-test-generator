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
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private String category;
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable
	(
	 name = "has_product_tag",
	 joinColumns = @JoinColumn(name = "P_id"),
	 inverseJoinColumns = @JoinColumn(name = "T_id")
	)
	private List<Product_Tag> tags;
	private Boolean withdrawn;


	Product (String name, String description, String category, List<Product_Tag> tags, Boolean withdrawn) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.tags = tags;
		this.withdrawn = withdrawn;
	}

	public Product(){};

	public ProductDTO _convertToProductDTO() {
		List<String> tagList = new ArrayList<String>();
		for (Product_Tag t: tags) {
			tagList.add(t.getName());
		}
		return new ProductDTO(id, name, description, category,
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

	public List<Product_Tag> getTags() {
		return tags;
	}

	public void setTags(List<Product_Tag> tags) {
		this.tags = tags;
	}

	public void setTagsFromString(List<String> tags) {
		this.tags.clear();
		for (String s: tags) {
			this.tags.add(new Product_Tag(s));
		}
	}

	public Boolean getWithdrawn() {
		return withdrawn;
	}

	public void setWithdrawn(Boolean withdrawn) {
		this.withdrawn = withdrawn;
	}
}
