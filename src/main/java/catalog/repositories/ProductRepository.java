package catalog.repositories;

import catalog.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Product specific data layer operations
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Retrieves product by its name
     *
     * @param name product name
     * @return product by given name
     */
    Product findOneByName(String name);

    /**
     * Retrieves list of products by its category names
     *
     * @param name category name
     * @return list of products by given category name
     */
    List<Product> findByCategoryName(String name);

    /**
     * Retrieves list of all products with associated images
     *
     * @return list of all products with associated images
     */
    List<Product> findAllByImageIsNotNullOrderById();

}
