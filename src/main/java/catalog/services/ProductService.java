package catalog.services;

import catalog.domain.Product;
import catalog.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<Product> getAll();

    Product getById(Long id);

    List<Product> getByCategory(String category);

    Product createProduct(ProductDTO dto);

    Product updateProduct(ProductDTO dto);

    void deleteProduct(Long id);

    List<Product> getAllWithImages();

    void addImageToProduct(String product, String fileName);

}
