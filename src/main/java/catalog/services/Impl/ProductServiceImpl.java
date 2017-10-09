package catalog.services.Impl;

import catalog.domain.Category;
import catalog.domain.Product;
import catalog.dto.ProductDTO;
import catalog.repositories.CategoryRepository;
import catalog.repositories.ProductRepository;
import catalog.services.FileService;
import catalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Product specific business layer operations
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get list of all products
     *
     * @return list of products
     */
    @Override
    public List<Product> getAll() {
        return productRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    /**
     * Get product by its id
     *
     * @param id product id
     * @return entity if exist, otherwise null
     */
    @Override
    public Product getById(Long id) {
        return productRepository.findOne(id);
    }

    @Override
    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    /**
     * Create product by DTO object
     *
     * @param dto object containing properties for new entity
     * @return created entity
     */
    @Override
    public Product createProduct(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setManufacturer(dto.getManufacturer());
        product.setPrice(dto.getPrice());
        product.setCreated(new Date());
        product.setImage(dto.getImage());

        Category category = Optional.ofNullable(dto.getCategory())
                .map(name -> categoryRepository.findOneByName(name))
                .orElse(null);
        product.setCategory(category);

        return productRepository.save(product);
    }

    /**
     * Update product by DTO object
     *
     * @param dto object containing properties for update in existing entity
     * @return updated entity
     */
    @Override
    public Product updateProduct(ProductDTO dto) {
        Long id = Optional.ofNullable(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("Id field must be presented in DTO"));
        Product product = productRepository.findOne(id);
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setManufacturer(dto.getManufacturer());
        product.setPrice(dto.getPrice());

        Category category = Optional.ofNullable(dto.getCategory())
                .map(name -> categoryRepository.findOneByName(name))
                .orElse(null);
        product.setCategory(category);

        return productRepository.save(product);
    }

    /**
     * Delete product by its id.
     *
     * @param id id of deleted product
     */
    @Override
    public void deleteProduct(Long id) {
        productRepository.delete(id);
    }

    /**
     * Retrieves list of all products with associated images
     *
     * @return list of all products with associated images
     */
    @Override
    public List<Product> getAllWithImages() {
        return productRepository.findAllByImageIsNotNullOrderById();
    }

    /**
     * Associate product with uploaded file
     *
     * @param name name of product to link with file
     * @param fileName file name of uploaded file
     */
    @Override
    @Transactional
    public void addImageToProduct(String name, String fileName) {
        Product product = productRepository.findOneByName(name);
        if (product == null) {
            throw new NoSuchElementException("Product does not exist");
        }

        Optional.ofNullable(product.getImage())
                .ifPresent(fileService::deleteFile);

        product.setImage(fileName);
        productRepository.save(product);
    }
}
