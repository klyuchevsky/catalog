package catalog.dto;

import catalog.domain.Category;
import catalog.domain.Product;

import java.util.Date;
import java.util.Optional;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private String manufacturer;
    private Double price;
    private Date created;
    private String image;
    private String category;

    public static ProductDTO from(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setManufacturer(product.getManufacturer());
        dto.setPrice(product.getPrice());
        dto.setCreated(product.getCreated());
        dto.setImage(product.getImage());

        Optional.ofNullable(product.getCategory())
                .map(Category::getName)
                .ifPresent(dto::setCategory);

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
