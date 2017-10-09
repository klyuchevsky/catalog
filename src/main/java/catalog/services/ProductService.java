package catalog.services;

import catalog.domain.Product;
import catalog.dto.ProductDTO;

import java.util.List;

/**
 * Product specific business layer operations
 */
public interface ProductService {

    /**
     * Get list of all products
     *
     * @return list of products
     */
    List<Product> getAll();

    /**
     * Get product by its id
     *
     * @param id product id
     * @return entity if exist, otherwise null
     */
    Product getById(Long id);

    List<Product> getByCategory(String category);

    /**
     * Create product by DTO object
     *
     * @param dto object containing properties for new entity
     * @return created entity
     */
    Product createProduct(ProductDTO dto);

    /**
     * Update product by DTO object
     *
     * @param dto object containing properties for update in existing entity
     * @return updated entity
     */
    Product updateProduct(ProductDTO dto);

    /**
     * Delete product by its id.
     *
     * @param id id of deleted product
     */
    void deleteProduct(Long id);

    /**
     * Retrieves list of all products with associated images
     *
     * @return list of all products with associated images
     */
    List<Product> getAllWithImages();

    /**
     * Associate product with uploaded file
     *
     * @param name name of product to link with file
     * @param fileName file name of uploaded file
     */
    void addImageToProduct(String name, String fileName);

}
