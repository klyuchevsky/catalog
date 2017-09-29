package catalog.repositories;

import catalog.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Category specific data layer operations
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Retrieves category by its name
     *
     * @param name category name
     * @return category by given name
     */
    Category findOneByName(String name);

}
